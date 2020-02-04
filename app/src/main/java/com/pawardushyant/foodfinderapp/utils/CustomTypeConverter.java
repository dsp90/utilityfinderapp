package com.pawardushyant.foodfinderapp.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pawardushyant.foodfinderapp.repository.models.Geometry;
import com.pawardushyant.foodfinderapp.repository.models.PlacePhoto;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import androidx.room.TypeConverter;

public class CustomTypeConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<String> stringToSomeObj(String data){
        if (data == null){
            return Collections.emptyList();
        }

        Type arrayType = new TypeToken<List<String>>(){}.getType();

        return gson.fromJson(data, arrayType);
    }

    @TypeConverter
    public static String listToString(List<String> list){
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<PlacePhoto> stringToPlacePhoto(String data){
        if (data == null){
            return Collections.emptyList();
        }

        Type arrayType = new TypeToken<List<PlacePhoto>>(){}.getType();

        return gson.fromJson(data, arrayType);
    }

    @TypeConverter
    public static String placePhotosListToString(List<PlacePhoto> list){
        return gson.toJson(list);
    }

    @TypeConverter
    public static Geometry geometryObjToString(String data){
        if (data == null){
            return new Geometry();
        }

        Type objType = new TypeToken<Geometry>(){}.getType();

        return gson.fromJson(data, objType);
    }

    @TypeConverter
    public static String objToString(Geometry geometry){ return gson.toJson(geometry); }
}
