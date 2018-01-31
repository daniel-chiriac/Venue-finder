package com.chiriacd.venuefinder.foursquare.api;


import android.os.Parcel;
import android.os.Parcelable;

public class GroupItem {

    public Venue venue;

    public static class Venue implements Parcelable {

        float rating;
        String name;

        protected Venue(Parcel in) {
            rating = in.readFloat();
            name = in.readString();
        }

        public float getRating() {
            return rating;
        }

        public String getName() {
            return name;
        }

        public static final Creator<Venue> CREATOR = new Creator<Venue>() {
            @Override
            public Venue createFromParcel(Parcel in) {
                return new Venue(in);
            }

            @Override
            public Venue[] newArray(int size) {
                return new Venue[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeFloat(rating);
            dest.writeString(name);
        }
    }
}
