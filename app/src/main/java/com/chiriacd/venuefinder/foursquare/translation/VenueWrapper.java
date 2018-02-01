package com.chiriacd.venuefinder.foursquare.translation;

import android.os.Parcel;
import android.os.Parcelable;

import com.chiriacd.venuefinder.foursquare.api.Category;
import com.chiriacd.venuefinder.foursquare.api.Icon;
import com.chiriacd.venuefinder.foursquare.api.Venue;

import io.reactivex.internal.operators.observable.ObservableScan;

public class VenueWrapper implements Parcelable {

    private String name;
    private float rating;
    private String iconUrl;

    public VenueWrapper(Venue venue) {
        this.name = venue.getName();
        this.rating = venue.getRating();
        initIcon(venue);
    }

    protected VenueWrapper(Parcel in) {
        name = in.readString();
        rating = in.readFloat();
        iconUrl = in.readString();
    }

    private void initIcon(Venue venue) {
        ObservableScan.fromIterable(venue.getCategories())
                .filter(Category::isPrimary)
                .map(Category::getIcon)
                .blockingSubscribe(this::buildIconUrl);
    }

    private void buildIconUrl(Icon icon) {
        iconUrl = icon.getPrefix() +
                Icon.Background.GREY.value +
                Icon.Size.FOUR.value +
                icon.getSuffix();
    }

    public String getName() {
        return name;
    }

    public float getRating() {
        return rating;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeFloat(rating);
        dest.writeString(iconUrl);
    }

    public static final Creator<VenueWrapper> CREATOR = new Creator<VenueWrapper>() {
        @Override
        public VenueWrapper createFromParcel(Parcel in) {
            return new VenueWrapper(in);
        }

        @Override
        public VenueWrapper[] newArray(int size) {
            return new VenueWrapper[size];
        }
    };
}
