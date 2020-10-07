package com.example.sampleretainapp;

import android.app.Application;

public class App extends Application {

    public Repository getRepository() {
        return Repository.getInstance(this);
    }
}
