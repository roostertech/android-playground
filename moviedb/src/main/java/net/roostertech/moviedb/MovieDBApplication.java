package net.roostertech.moviedb;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

public class MovieDBApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
