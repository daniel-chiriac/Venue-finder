package com.chiriacd.whitbread;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.chiriacd.whitbread.foursquare.FoursquareService;
import com.chiriacd.whitbread.foursquare.api.GroupItem;
import com.chiriacd.whitbread.whitbread.R;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class WhitbreadActivity extends Activity {

    private static final String TAG = WhitbreadActivity.class.getName();

    @Inject FoursquareService foursquareService;

    private EditText placeInput;
    private RecyclerView recyclerView;
    private Disposable foursquareSubscription;
    private RecommendedVenuesAdapter venuesAdapter;
    private View loadingIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        initUI(this);


        RxTextView.textChanges(placeInput)
                .doOnNext(c -> {
                    if (c!=null && c.length() ==0) {
                        onFinishLoading();
                    } else {
                        onStartLoading();
                    }
                })
                .debounce(2, TimeUnit.SECONDS)
                .filter(c -> c != null && c.length() > 0)
                .subscribe(c -> foursquareSubscription = foursquareService.getItem(c.toString())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(response -> response.getResponse().get(0).getItems())
                        .flatMapIterable(item -> item)
                        .map(item-> item.venue)
                        .toList()
                        .onErrorReturn(throwable -> {
                            onError();
                            return new ArrayList<>();
                        })
                        .doOnSuccess(this::onItemsRetrieved)
                        .subscribeOn(Schedulers.io())
                        .subscribe());
    }

    private void onItemsRetrieved(List<GroupItem.Venue> venues) {
        onFinishLoading();
        venuesAdapter.setData(venues);
        Log.i(TAG, "items received");
    }

    private void onError() {
        onFinishLoading();
    }

    private void initUI(Context context) {
        setContentView(R.layout.whitbread_activity);
        loadingIndicator = findViewById(R.id.loading_indicator);
        placeInput = findViewById(R.id.place_input);
        recyclerView = findViewById(R.id.recycler_view);
        venuesAdapter = new RecommendedVenuesAdapter();
        recyclerView.setAdapter(venuesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void onStartLoading() {
        venuesAdapter.clear();
        loadingIndicator.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void onFinishLoading() {
        loadingIndicator.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (foursquareSubscription != null) foursquareSubscription.dispose();
    }
}
