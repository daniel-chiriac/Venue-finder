package com.chiriacd.whitbread.injection.app;

import com.chiriacd.whitbread.WhitbreadActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface WhitbreadActivitySubcomponent extends AndroidInjector<WhitbreadActivity> {

    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<WhitbreadActivity> {

    }
}
