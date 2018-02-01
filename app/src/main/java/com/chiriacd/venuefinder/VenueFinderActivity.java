package com.chiriacd.venuefinder;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.chiriacd.venuefinder.foursquare.FoursquareServiceWrapper;
import com.chiriacd.venuefinder.foursquare.translation.KnownGroupTypes;
import com.chiriacd.venuefinder.foursquare.translation.VenueWrapper;
import com.chiriacd.venuefinder.helpers.RxFilters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VenueFinderActivity extends Activity {

    private static final String TAG = VenueFinderActivity.class.getName();

    private static final String VENUES_KEY = "venues";
    private static final String LOCATION_KEY = "location";

    @Inject FoursquareServiceWrapper foursquareService;
    @Inject VenueFinderActivityView venueFinderActivityView;

    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        initRx();
        setupViewListeners();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        List<VenueWrapper> displayedVenues = venueFinderActivityView.getDisplayedVenues();
        outState.putParcelableArrayList(VENUES_KEY, new ArrayList<>(displayedVenues));
        outState.putString(LOCATION_KEY, venueFinderActivityView.getDisplayedLocation());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<VenueWrapper> venues = savedInstanceState.getParcelableArrayList(VENUES_KEY);
        String location = savedInstanceState.getString(LOCATION_KEY);
        venueFinderActivityView.updateVenues(location, venues);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void initRx() {
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(venueFinderActivityView);
    }

    private void setupViewListeners() {
        compositeDisposable.add(
                venueFinderActivityView.getTextChangesObservable()
                        .debounce(2, TimeUnit.SECONDS)
                        .filter(RxFilters::filterNonEmpty)
                        .map(CharSequence::toString)
                        .filter(s -> !s.equals(venueFinderActivityView.getDisplayedLocation()))
                        .subscribe(this::getVenuesByLocation));
    }

    private void getVenuesByLocation(final String location) {
        compositeDisposable.add(
                foursquareService.getVenuesByType(location, KnownGroupTypes.RECOMMENDED)
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorReturn(throwable -> {
                            venueFinderActivityView.onVenueRetrievingError();
                            return Collections.emptyList();
                        })
                        .doOnSuccess(venues -> onItemsRetrieved(location, venues))
                        .subscribeOn(Schedulers.io())
                        .subscribe());
    }

    private void onItemsRetrieved(String location, List<VenueWrapper> venues) {
        venueFinderActivityView.updateVenues(location, venues);
        Log.i(TAG, "items received");
    }
}
