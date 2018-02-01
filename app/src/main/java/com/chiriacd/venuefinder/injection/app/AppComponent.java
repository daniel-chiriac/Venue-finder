package com.chiriacd.venuefinder.injection.app;


import com.chiriacd.venuefinder.injection.api.FoursquareApiModule;
import com.chiriacd.venuefinder.injection.app.modules.AppContextModule;
import com.chiriacd.venuefinder.injection.app.modules.VenueFinderActivityModule;

import dagger.Component;


@Component(modules = {VenueFinderActivityModule.class, FoursquareApiModule.class,
        AppContextModule.class})
@AppScope
public interface AppComponent {
    void inject(MyApp application);
}
