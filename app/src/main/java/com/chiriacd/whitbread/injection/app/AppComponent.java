package com.chiriacd.whitbread.injection.app;


import com.chiriacd.whitbread.injection.api.FoursquareApiModule;

import dagger.Component;


@Component(modules = {WhitbreadActivityModule.class, FoursquareApiModule.class, ContextModule.class})
@AppScope
public interface AppComponent {
    void inject(MyApp application);
}
