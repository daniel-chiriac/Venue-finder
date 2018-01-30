package com.chiriacd.whitbread.foursquare.api;


public class GroupItem {

    public Venue venue;

    public static class Venue {
        float rating;
        String name;

        public float getRating() {
            return rating;
        }

        public String getName() {
            return name;
        }
    }


}
