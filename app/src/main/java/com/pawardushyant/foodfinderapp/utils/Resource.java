package com.pawardushyant.foodfinderapp.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {

    @Nullable
    public final T data;

    @Nullable
    public static String message;

    @Nullable
    public static Status status;

    public Resource(@Nullable Status status, @Nullable T data, @Nullable String message){
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data){
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> loading(@Nullable T data){
        return new Resource<>(Status.LOADING, data, null);
    }

    public static <T> Resource<T> error(@NonNull String message, @Nullable T data){
        return new Resource<>(Status.ERROR, data, message);
    }

    public enum Status{SUCCESS, LOADING, ERROR}
}