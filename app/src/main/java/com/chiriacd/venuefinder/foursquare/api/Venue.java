package com.chiriacd.venuefinder.foursquare.api;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Venue implements Parcelable {

    private float rating;
    private String name;
    private List<Category> categories;

    protected Venue(Parcel in) {
        rating = in.readFloat();
        name = in.readString();
        categories = in.createTypedArrayList(Category.CREATOR);
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

    public float getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public List<Category> getCategories(){
        return categories;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(rating);
        dest.writeString(name);
        dest.writeTypedList(categories);
    }
}
