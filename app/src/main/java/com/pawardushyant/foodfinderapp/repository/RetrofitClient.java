package com.pawardushyant.foodfinderapp.repository;

import com.pawardushyant.foodfinderapp.utils.LiveDataCallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitClient {

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com")
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static SearchFoodAndRestaurantsApi api =
            retrofit.create(SearchFoodAndRestaurantsApi.class);

    static SearchFoodAndRestaurantsApi getFoodAndRestaurants(){
        return api;
    }

}