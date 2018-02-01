package com.chiriacd.venuefinder.foursquare.api;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable{
    private Icon icon;
    private boolean primary;

    protected Category(Parcel in) {
        icon = in.readParcelable(Icon.class.getClassLoader());
        primary = in.readByte() != 0;
    }

    public boolean isPrimary() {
        return primary;
    }

    public Icon getIcon() {
        return icon;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(icon, flags);
        dest.writeByte((byte) (primary ? 1 : 0));
    }
}
