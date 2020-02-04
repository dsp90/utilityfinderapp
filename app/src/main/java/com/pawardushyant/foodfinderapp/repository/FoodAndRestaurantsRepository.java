package com.pawardushyant.foodfinderapp.repository;

import android.content.Context;

import com.pawardushyant.foodfinderapp.data.ResultsDatabase;
import com.pawardushyant.foodfinderapp.data.SearchResultsDao;
import com.pawardushyant.foodfinderapp.repository.models.Geometry;
import com.pawardushyant.foodfinderapp.repository.models.LocationCoordinates;
import com.pawardushyant.foodfinderapp.repository.models.PlacePhoto;
import com.pawardushyant.foodfinderapp.repository.models.ResultHits;
import com.pawardushyant.foodfinderapp.repository.models.SearchResultsResponse;
import com.pawardushyant.foodfinderapp.repository.response.ApiResponse;
import com.pawardushyant.foodfinderapp.utils.AppExecutors;
import com.pawardushyant.foodfinderapp.utils.Commons;
import com.pawardushyant.foodfinderapp.utils.Constants;
import com.pawardushyant.foodfinderapp.utils.NetworkBoundResource;
import com.pawardushyant.foodfinderapp.utils.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class FoodAndRestaurantsRepository {

    private static FoodAndRestaurantsRepository instance;
    private SearchResultsDao dao;

    private FoodAndRestaurantsRepository(Context context){
        dao = ResultsDatabase.getInstance(context).getSearchResultsDao();
    }

    public static FoodAndRestaurantsRepository getInstance(Context mContext){
        if (instance == null){
            instance = new FoodAndRestaurantsRepository(mContext);
        }
        return instance;
    }

    public LiveData<Resource<List<ResultHits>>> searchFoodRestaurantsApi(final String location,
                                                                         final String radius,
                                                                         final String query){
        return new NetworkBoundResource<List<ResultHits>,
                SearchResultsResponse>(AppExecutors.getInstance()){
            @Override
            protected void saveCallResult(@NonNull SearchResultsResponse item) {
                if (item.getResults() != null){

                    ResultHits[] hits = new ResultHits[item.getResults().size()];

                    int index = 0;
                    for (long rowid: dao.insertResults(item.getResults().toArray(hits))){
                        if (rowid == -1){

//                            Commons.getImageUrl(hits[index]
//                                    .getPhotos().get(0).getPhoto_reference())

                            List<PlacePhoto> photos = hits[index].getPhotos();
                            String imageurl = (photos != null && photos.size() > 0 )?
                                    Commons.getImageUrl(photos.get(0).getPhoto_reference()) :
                                    Constants.DEFAULT_IMAGE_URL;

                            dao.updateResults(
                                    hits[index].getId(),
                                    hits[index].getName(),
                                    hits[index].getRating(),
                                    hits[index].getVicinity(),
                                    imageurl
                            );
                        }
                        index ++;
                    }
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ResultHits> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<ResultHits>> loadFromdb() {
//                String[] coords = (location!=null) ? location.split(",") :
//                        Constants.LOCATION.split(",");
//                Geometry geometry = new Geometry();
//                geometry.setLocationCoordinates(new LocationCoordinates(coords[0], coords[1]));
//                return dao.searchFoodAndrestaurants(query, geometry);
//                return dao.searchFoodAndrestaurants(query, Constants.LOCATION);
                return dao.searchFoodAndrestaurants(query);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<SearchResultsResponse>> createCall() {
                return RetrofitClient.getFoodAndRestaurants()
                        .getSearchQueryResponse(
                                location == null ? Constants.LOCATION : location,
                                radius == null ? Constants.RADIUS : radius,
                                query,
                                Constants.API_KEY
                        );
            }
        }.getAsLiveData();
    }
}
