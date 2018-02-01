package com.chiriacd.venuefinder.foursquare.api;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Pieces needed to construct category icons at various sizes.
 * Combine prefix with a size (32, 44, 64, and 88 are available) and suffix,
 * e.g. https://foursquare.com/img/categories/food/default_64.png. To get an image with
 * a gray background, use bg_ before the size,
 * e.g. https://foursquare.com/img/categories_v2/food/icecream_bg_32.png
 */
public class Icon implements Parcelable {

    private String prefix;
    private String suffix;

    public enum Size {
        //only one value used, but listing all sizes for future needs
        ONE(32), TWO(44), THREE(64), FOUR(88);

        public final int value;

        Size(int value) {
            this.value = value;
        }
    }

    public enum Background {
        DEFAULT(""), GREY("_bg");

        public final String value;

        Background(String value) {
            this.value = value;
        }
    }

    protected Icon(Parcel in) {
        prefix = in.readString();
        suffix = in.readString();
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getSize() {
        return Size.ONE.value;
    }

    public static final Creator<Icon> CREATOR = new Creator<Icon>() {
        @Override
        public Icon createFromParcel(Parcel in) {
            return new Icon(in);
        }

        @Override
        public Icon[] newArray(int size) {
            return new Icon[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(prefix);
        dest.writeString(suffix);
    }
}
