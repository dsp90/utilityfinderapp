package com.pawardushyant.foodfinderapp.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static AppExecutors appExecutorsInstance;

    private AppExecutors(){}

    public static AppExecutors getInstance(){
        if (appExecutorsInstance == null){
            appExecutorsInstance = new AppExecutors();
        }
        return appExecutorsInstance;
    }

    private final Executor diskIO = Executors.newSingleThreadExecutor();
    private final Executor mainThreadExecutor = new MainThreadExecutor();

    Executor getDiskIO(){return diskIO;}

    Executor getMainThreadExecutor(){
        return mainThreadExecutor;
    }

    private class MainThreadExecutor implements Executor{

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }
}
