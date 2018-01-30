package com.chiriacd.whitbread.foursquare;


import com.chiriacd.whitbread.foursquare.api.VenueRecommendations;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoursquareService {

    @GET("venues/explore")
    Observable<VenueRecommendations> getItem(@Query("near") String near);
}
