package com.pawardushyant.foodfinderapp.repository;

import com.pawardushyant.foodfinderapp.repository.models.SearchResultsResponse;
import com.pawardushyant.foodfinderapp.repository.response.ApiResponse;

import androidx.lifecycle.LiveData;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchFoodAndRestaurantsApi {

    @GET("maps/api/place/nearbysearch/json")
    LiveData<ApiResponse<SearchResultsResponse>> getSearchQueryResponse(
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("type") String query,
            @Query("key") String key
    );
}
