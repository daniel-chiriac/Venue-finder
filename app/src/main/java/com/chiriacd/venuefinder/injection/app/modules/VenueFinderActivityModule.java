package com.chiriacd.venuefinder.injection.app.modules;

import android.app.Activity;

import com.chiriacd.venuefinder.VenueFinderActivity;
import com.chiriacd.venuefinder.injection.app.VenueFinderActivitySubcomponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = VenueFinderActivitySubcomponent.class)
public abstract class VenueFinderActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(VenueFinderActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourActivityInjectorFactory(VenueFinderActivitySubcomponent.Builder builder);
}

