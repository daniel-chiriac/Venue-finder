package com.chiriacd.venuefinder.injection.app;


import com.chiriacd.venuefinder.injection.api.FoursquareApiModule;

import dagger.Component;


@Component(modules = {VenueFinderActivityModule.class, FoursquareApiModule.class, ContextModule.class})
@AppScope
public interface AppComponent {
    void inject(MyApp application);
}
