package com.chiriacd.venuefinder.helpers;


import com.chiriacd.venuefinder.foursquare.api.Group;
import com.chiriacd.venuefinder.foursquare.api.local.KnownGroupTypes;

public class RxFilters {

    //todo not sure it's the best place to have all the filters in same class
    //todo they have different purposes, and should probably be separated

    public static boolean filterGroupByType(Group group, KnownGroupTypes type) {
        return type.value.equals(group.getName());
    }

    public static boolean filterNonEmpty(CharSequence c) {
        return c != null && c.length() > 0;
    }
}
