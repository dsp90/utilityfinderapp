package com.pawardushyant.foodfinderapp.data;

import com.pawardushyant.foodfinderapp.repository.models.ResultHits;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface SearchResultsDao {

    @Insert(onConflict = IGNORE)
    long[] insertResults(ResultHits... results);

    @Insert(onConflict = REPLACE)
    void insertSearchResult(ResultHits resultHit);

    @Query("UPDATE searchresults SET name = :name, rating = :rating, vicinity = :vicinity," +
            " imageurl = :imageurl WHERE id = :id ")
    void updateResults(String id, String name, String rating, String vicinity, String imageurl);

    @Query("SELECT * FROM searchresults WHERE types LIKE '%' || :type || '%'")
    LiveData<List<ResultHits>> searchFoodAndrestaurants(String type);

    @Query("SELECT * FROM searchresults where id = :id")
    LiveData<ResultHits> getResults(String id);

}
