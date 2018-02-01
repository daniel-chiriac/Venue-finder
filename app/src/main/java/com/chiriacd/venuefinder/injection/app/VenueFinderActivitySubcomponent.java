package com.chiriacd.venuefinder.injection.app;

import com.chiriacd.venuefinder.VenueFinderActivity;
import com.chiriacd.venuefinder.VenueFinderActivityView;

import dagger.Provides;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface VenueFinderActivitySubcomponent extends AndroidInjector<VenueFinderActivity> {

    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<VenueFinderActivity> {

    }
}
