package com.chiriacd.venuefinder.foursquare.api;


import java.util.List;

public class Group {

    private String type;

    /**
     * The role of type and name seems to be reversed, in contrast with documentation.
     * We will use name to check for the type of group, while type will contain human readable name.
     */
    private String name;

    private List<GroupItem> items;

    public List<GroupItem> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }
}
