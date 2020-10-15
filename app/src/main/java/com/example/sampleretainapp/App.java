package com.example.sampleretainapp;

import android.app.Application;

import com.example.sampleretainapp.db.AppDatabase;

public class App extends Application {

    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppExecutors = new AppExecutors();
    }

    public Repository getRepository() {
        return Repository.getInstance(this,
                AppDatabase.getInstance(this,
                        mAppExecutors),mAppExecutors);
    }
}
