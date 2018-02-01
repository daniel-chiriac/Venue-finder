package com.chiriacd.venuefinder.injection.app.modules;

import android.content.Context;

import com.chiriacd.venuefinder.VenueFinderActivity;
import com.chiriacd.venuefinder.VenueFinderActivityView;
import com.chiriacd.venuefinder.injection.app.AppScope;

import dagger.Module;
import dagger.Provides;


@Module
public class AppContextModule {

    private Context context;

    public AppContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @AppScope
    Context provideContext() {
        return context;
    }
}
