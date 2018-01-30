package com.chiriacd.whitbread;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.chiriacd.whitbread.foursquare.FoursquareService;
import com.chiriacd.whitbread.whitbread.R;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjection;


public class WhitbreadActivity extends Activity {

    private static final String TAG = WhitbreadActivity.class.getName();
    @Inject FoursquareService foursquareService;

    private EditText placeInput;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        initUI();

        RxTextView.textChanges(placeInput)
                .debounce(2, TimeUnit.SECONDS)
                .filter(c->c != null && c.length()>=0)
                .subscribe(c -> {
//                    foursquareService.getItem(c.toString());
                    Log.i(TAG, c.toString());
                });
    }

    private void initUI() {
        setContentView(R.layout.whitbread_activity);
        placeInput = findViewById(R.id.place_input);
        recyclerView = findViewById(R.id.recycler_view);
    }


}
