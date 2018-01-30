package com.chiriacd.whitbread.injection.app;

import android.app.Activity;

import com.chiriacd.whitbread.WhitbreadActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = WhitbreadActivitySubcomponent.class)
public abstract class WhitbreadActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(WhitbreadActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourActivityInjectorFactory(WhitbreadActivitySubcomponent.Builder builder);
}

