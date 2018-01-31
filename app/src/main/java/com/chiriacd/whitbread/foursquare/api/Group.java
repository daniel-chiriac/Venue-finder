package com.chiriacd.whitbread.foursquare.api;


import java.util.List;

public class Group {

    String type;

    /**
     * The role of type and name seems to be reversed, in contrast with documentation.
     * We will use name to check for the type of group, while type will contain human readable name.
     */
    String name;

    List<GroupItem> items;

    public List<GroupItem> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }
}
