package com.chiriacd.venuefinder.injection.app;

import android.app.Activity;
import android.app.Application;

import com.chiriacd.venuefinder.injection.app.modules.AppContextModule;
import com.facebook.drawee.backends.pipeline.Fresco;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class MyApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder()
                .appContextModule(new AppContextModule(this))
                .build()
                .inject(this);
        Fresco.initialize(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }
}
