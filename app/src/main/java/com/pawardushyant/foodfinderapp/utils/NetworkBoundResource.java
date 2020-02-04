package com.pawardushyant.foodfinderapp.utils;

import com.pawardushyant.foodfinderapp.repository.response.ApiResponse;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public abstract class NetworkBoundResource<ResultType, RequestType> {

    private  static final String TAG = "NetworkBoundResource";
    private AppExecutors appExecutors;
    private MediatorLiveData<Resource<ResultType>> results = new MediatorLiveData<>();

    protected NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        init();
    }

    private void init() {
        results.setValue(Resource.loading(null));

        final LiveData<ResultType> dbSource = loadFromdb();

        results.addSource(dbSource, resultType -> {
            results.removeSource(dbSource);

            if (shouldFetch(resultType)) {
                fetchFromNetwork(dbSource);
            } else {
                results.addSource(dbSource, resultType1 -> setValue(Resource.success(resultType1)));
            }
        });
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {

        results.addSource(dbSource, resultType -> results.setValue(Resource.loading(resultType)));

        final LiveData<ApiResponse<RequestType>> apiResponse = createCall();

        results.addSource(apiResponse, requestTypeApiResponse -> {
            results.removeSource(dbSource);
            results.removeSource(apiResponse);

            if (requestTypeApiResponse instanceof ApiResponse.ApiSuccessResponse) {
                appExecutors.getDiskIO().execute(() -> {
                    saveCallResult((RequestType) processResponse((ApiResponse.ApiSuccessResponse) requestTypeApiResponse));

                    appExecutors.getMainThreadExecutor().execute(() -> results.addSource(loadFromdb()
                            , resultType -> setValue(Resource.success(resultType))));
                });
            } else if (requestTypeApiResponse instanceof ApiResponse.ApiEmptyResponse) {
                appExecutors.getMainThreadExecutor().execute(() -> results.addSource(loadFromdb()
                        , resultType -> setValue(Resource.success(resultType))));
            } else if (requestTypeApiResponse instanceof ApiResponse.ApiErrorResponse) {
                results.addSource(dbSource, resultType -> setValue(Resource.error(
                        ((ApiResponse.ApiErrorResponse) requestTypeApiResponse).getErrorMessage(),
                        resultType
                )));
            }
        });
    }

    private ResultType processResponse(ApiResponse.ApiSuccessResponse response) {
        return (ResultType) response.getBody();
    }

    private void setValue(Resource<ResultType> newValue) {
        if (results.getValue() != newValue) {
            results.setValue(newValue);
        }
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromdb();

    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    public final LiveData<Resource<ResultType>> getAsLiveData() {
        return results;
    }
}
