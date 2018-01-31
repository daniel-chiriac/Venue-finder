package com.chiriacd.whitbread.foursquare.api;


import java.util.List;

public class VenueRecommendations {

    Response response;

    public Response getResponse() {
        return response;
    }

    public static class Response {
        List<Group> groups;

        public List<Group> getGroups() {
            return groups;
        }
    }
}
