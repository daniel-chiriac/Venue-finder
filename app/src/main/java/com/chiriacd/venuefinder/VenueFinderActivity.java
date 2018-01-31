package com.chiriacd.venuefinder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chiriacd.venuefinder.foursquare.FoursquareService;
import com.chiriacd.venuefinder.foursquare.api.Group;
import com.chiriacd.venuefinder.foursquare.api.GroupItem;
import com.chiriacd.venuefinder.foursquare.api.VenueRecommendations;
import com.chiriacd.venuefinder.foursquare.api.local.KnownGroupTypes;
import com.chiriacd.venuefinder.helpers.RxFilters;
import com.jakewharton.rxbinding2.widget.RxTextView;

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

    @Inject
    FoursquareService foursquareService;

    private EditText locationEditText;
    private TextView noResultsView;
    private RecyclerView recyclerView;
    private RecommendedVenuesAdapter venuesAdapter;
    private View loadingIndicator;

    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        initRx();
        initUI(this);
        setupViewListeners();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(VENUES_KEY, new ArrayList<>(venuesAdapter.getData()));
        outState.putString(LOCATION_KEY, venuesAdapter.getLocation());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<GroupItem.Venue> venues = savedInstanceState.getParcelableArrayList(VENUES_KEY);
        String location = savedInstanceState.getString(LOCATION_KEY);
        venuesAdapter.setData(location, venues);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    //region Setup/Init

    private void initRx() {
        compositeDisposable = new CompositeDisposable();
    }

    private void initUI(Context context) {
        setContentView(R.layout.venuefinder_activity);
        loadingIndicator = findViewById(R.id.loading_indicator);
        locationEditText = findViewById(R.id.place_input);
        noResultsView = findViewById(R.id.no_results_view);

        venuesAdapter = new RecommendedVenuesAdapter();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(venuesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void setupViewListeners() {
        compositeDisposable.add(
                RxTextView.textChanges(locationEditText)
                        .skip(2) //skip value on rotation; for some reason,
                        // when using skip(1) or skipInitialValue() it doesn't skip
                        .doOnNext(this::onLocationValueChanged)
                        .debounce(2, TimeUnit.SECONDS)
                        .filter(RxFilters::filterNonEmpty)
                        .map(CharSequence::toString)
                        .filter(s -> !s.equals(venuesAdapter.getLocation()))
                        .subscribe(this::getVenuesByLocation));
    }
    //endregion

    //region User Interaction

    private void onLocationValueChanged(CharSequence c) {
        if (c != null && c.length() > 0) {
            onStartLoading();
        } else {
            onFinishLoading();
        }
    }

    //endregion

    //region User Interface
    private void onVenueRetrievingError() {
        onFinishLoading();
        showNoResultsView();
    }

    private void showNoResultsView() {
        noResultsView.setVisibility(View.VISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void hideNoResultsView() {
        noResultsView.setVisibility(View.INVISIBLE);
    }

    private void onStartLoading() {
        venuesAdapter.clear();
        loadingIndicator.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        hideNoResultsView();
    }

    private void onFinishLoading() {
        loadingIndicator.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    //endregion

    //region Network Methods

    private void getVenuesByLocation(final String location) {
        compositeDisposable.add(
                foursquareService.getRecommendedVenues(location)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(VenueRecommendations::getResponse)
                        .filter(response -> response != null)
                        .map(VenueRecommendations.Response::getGroups)
                        .flatMapIterable(groupList -> groupList)
                        .filter(group -> RxFilters.filterGroupByType(group, KnownGroupTypes.RECOMMENDED))
                        .map(Group::getItems)
                        .flatMapIterable(item -> item)
                        .map(item -> item.venue)
                        .toList()
                        .onErrorReturn(throwable -> {
                            onVenueRetrievingError();
                            return Collections.emptyList();
                        })
                        .doOnSuccess(venues -> onItemsRetrieved(location, venues))
                        .subscribeOn(Schedulers.io())
                        .subscribe());
    }

    //endregion

    //region Data processing

    private void onItemsRetrieved(String location, List<GroupItem.Venue> venues) {
        onFinishLoading();
        venuesAdapter.setData(location, venues);
        Log.i(TAG, "items received");
    }

    //endregion
}
