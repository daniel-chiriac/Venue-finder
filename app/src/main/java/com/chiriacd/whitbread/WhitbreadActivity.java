package com.chiriacd.whitbread;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;

import com.chiriacd.whitbread.foursquare.FoursquareService;
import com.chiriacd.whitbread.foursquare.api.VenueRecommendations;
import com.chiriacd.whitbread.whitbread.R;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class WhitbreadActivity extends Activity {

    private static final String TAG = WhitbreadActivity.class.getName();
    @Inject
    FoursquareService foursquareService;

    private EditText placeInput;
    private RecyclerView recyclerView;
    private Disposable foursquareSubscription;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        initUI();

        RxTextView.textChanges(placeInput)
                .debounce(2, TimeUnit.SECONDS)
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(CharSequence c) throws Exception {
                        return c == null || c.length() > 0;
                    }
                })
                .subscribe(c -> {
                    foursquareSubscription = foursquareService.getItem(c.toString())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSuccess(this::onItemsRetrieved)
                            .doOnError(throwable -> Log.i(TAG, throwable.toString()))
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                    Log.i(TAG, c.toString());
                });
    }

    private void initUI() {
        setContentView(R.layout.whitbread_activity);
        placeInput = findViewById(R.id.place_input);
        recyclerView = findViewById(R.id.recycler_view);
    }

    void onItemsRetrieved(VenueRecommendations response) {
        Log.i(TAG, "items received");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (foursquareSubscription != null) foursquareSubscription.dispose();
    }
}
