package com.chiriacd.venuefinder;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chiriacd.venuefinder.foursquare.translation.VenueWrapper;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class VenueFinderActivityView implements Disposable {

    private EditText locationEditText;
    private TextView noResultsView;
    private RecyclerView recyclerView;
    private RecommendedVenuesAdapter venuesAdapter;
    private View loadingIndicator;

    private CompositeDisposable compositeDisposable;

    @Inject
    public VenueFinderActivityView(VenueFinderActivity activity) {
        compositeDisposable = new CompositeDisposable();
        initUI(activity);
    }

    private void initUI(VenueFinderActivity activity) {
        activity.setContentView(R.layout.venuefinder_activity);
        loadingIndicator = activity.findViewById(R.id.loading_indicator);
        locationEditText = activity.findViewById(R.id.place_input);
        noResultsView = activity.findViewById(R.id.no_results_view);

        venuesAdapter = new RecommendedVenuesAdapter();
        recyclerView = activity.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(venuesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        compositeDisposable.add(getTextChangesObservable()
                .skip(2) //skip value on rotation; for some reason,
                // when using skip(1) or skipInitialValue() it doesn't skip
                .subscribe(this::onLocationValueChanged));
    }

    public InitialValueObservable<CharSequence> getTextChangesObservable() {
        return RxTextView.textChanges(locationEditText);
    }

    void onVenueRetrievingError() {
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
        loadingIndicator.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        hideNoResultsView();
    }

    public void onFinishLoading() {
        loadingIndicator.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void onLocationValueChanged(CharSequence c) {
        if (c != null && c.length() > 0 && !c.toString().equals(getDisplayedLocation())) {
            onStartLoading();
        } else if (c == null || c.length() == 0) {
            venuesAdapter.clear();
            onFinishLoading();
        } else {
            onFinishLoading();
        }
    }

    public void updateVenues(String location, List<VenueWrapper> venues) {
        onFinishLoading();
        venuesAdapter.setData(location, venues);
    }

    public String getDisplayedLocation() {
        return venuesAdapter.getLocation();
    }

    /**
     * it is a bit odd for data to be stored in views, but will keep here for now
     *
     * @return
     */
    public List<VenueWrapper> getDisplayedVenues() {
        return venuesAdapter.getData();
    }

    @Override
    public void dispose() {
        compositeDisposable.dispose();
    }

    @Override
    public boolean isDisposed() {
        return compositeDisposable.isDisposed();
    }
}
