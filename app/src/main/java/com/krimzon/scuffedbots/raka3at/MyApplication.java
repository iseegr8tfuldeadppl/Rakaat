package com.krimzon.scuffedbots.raka3at;

import android.app.Application;

import net.time4j.android.ApplicationStarter;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationStarter.initialize(this, true); // with prefetch on background thread
    }
}