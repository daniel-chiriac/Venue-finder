package com.chiriacd.whitbread.foursquare;


import com.chiriacd.whitbread.foursquare.api.VenueRecommendationsResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoursquareService {

    @GET("venues/explore")
    Single<VenueRecommendationsResponse> getItem(@Query("near") String near);
}
