package com.pawardushyant.foodfinderapp.viewmodels;

import android.app.Application;

import com.pawardushyant.foodfinderapp.repository.FoodAndRestaurantsRepository;
import com.pawardushyant.foodfinderapp.repository.models.ResultHits;
import com.pawardushyant.foodfinderapp.utils.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class SearchResultsListViewModel extends AndroidViewModel {

    private static final String QUERY_EXHAUSTED = "No more results";
    private MediatorLiveData<Resource<List<ResultHits>>> resultHits = new MediatorLiveData<>();
    private FoodAndRestaurantsRepository repository;

    private String query;
    private String location;
    private String radius;
    private boolean cancelRequest;

    public SearchResultsListViewModel(@NonNull Application application) {
        super(application);
        repository = FoodAndRestaurantsRepository.getInstance(application);
    }

    public LiveData<Resource<List<ResultHits>>> getResultHits(){
        return resultHits;
    }

    public void doSearch(String location, String radius, String query) {
        this.location = location;
        this.radius = radius;
        this.query = query;
        doSearchImpl();
    }

    private void doSearchImpl() {
        final LiveData<Resource<List<ResultHits>>> resourceLiveData = repository
                .searchFoodRestaurantsApi(location,radius,query);
        resultHits.addSource(resourceLiveData, listResource -> {
            if (!cancelRequest) {
                if (listResource != null) {
                    if (listResource.status == Resource.Status.SUCCESS) {
                        if (listResource.data != null) {
                            if (listResource.data.size() == 0) {
                                new Resource<>(
                                        Resource.Status.ERROR,
                                        listResource.data,
                                        QUERY_EXHAUSTED
                                );
                            }
                        }
                        resultHits.removeSource(resourceLiveData);
                    } else if (listResource.status == Resource.Status.ERROR) {
                        resultHits.removeSource(resourceLiveData);
                    }
                    resultHits.setValue(listResource);
                } else {
                    resultHits.removeSource(resourceLiveData);
                }
            } else {
                resultHits.removeSource(resourceLiveData);
            }
        });
    }

    public void cancelRequest() {
        cancelRequest = true;
    }
}
