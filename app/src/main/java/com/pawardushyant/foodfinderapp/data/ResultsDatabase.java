package com.pawardushyant.foodfinderapp.data;

import android.content.Context;

import com.pawardushyant.foodfinderapp.repository.models.ResultHits;
import com.pawardushyant.foodfinderapp.utils.CustomTypeConverter;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {ResultHits.class}, version = 1, exportSchema = false)
@TypeConverters(CustomTypeConverter.class)
public abstract class ResultsDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "results_db";

    private static ResultsDatabase instance;

    public static ResultsDatabase getInstance(final Context mContext) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    mContext.getApplicationContext(),
                    ResultsDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract SearchResultsDao getSearchResultsDao();
}
