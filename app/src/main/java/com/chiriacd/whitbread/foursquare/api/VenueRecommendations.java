package com.chiriacd.whitbread.foursquare.api;


import java.util.List;

public class VenueRecommendations {

    Response response;

    public List<Group> getResponse() {
        return response.groups;
    }

    public static class Response {
        List<Group> groups;
    }
}
