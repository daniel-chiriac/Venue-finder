package com.chiriacd.venuefinder.foursquare;

import com.chiriacd.venuefinder.foursquare.api.Group;
import com.chiriacd.venuefinder.foursquare.api.GroupItem;
import com.chiriacd.venuefinder.foursquare.api.VenueRecommendations;
import com.chiriacd.venuefinder.foursquare.translation.KnownGroupTypes;
import com.chiriacd.venuefinder.foursquare.translation.VenueWrapper;
import com.chiriacd.venuefinder.helpers.RxFilters;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;


public class FoursquareServiceWrapper {

    private FoursquareService service;

    @Inject public FoursquareServiceWrapper(FoursquareService service) {
        this.service = service;
    }

    public Single<List<VenueWrapper>> getVenuesByType(String location, KnownGroupTypes type) {
        return service.getRecommendedVenues(location)
                .map(VenueRecommendations::getResponse)
                .map(VenueRecommendations.Response::getGroups)
                .flatMapIterable(groupList -> groupList)
                .filter(group -> RxFilters.filterGroupByType(group, type))
                .map(Group::getItems)
                .flatMapIterable(item -> item)
                .map(GroupItem::getVenue)
                .map(VenueWrapper::new)
                .toList();
    }
}
