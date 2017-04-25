package com.scausum.okhttpsample;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by xin on 17/2/23.
 */

public class LocationList implements Parcelable {

    public List<LocationInfo> topLocation;
    public List<Category> category;

    @Override
    public String toString() {
        return "LocationList{" +
                "topLocation=" + topLocation +
                ", category=" + category +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.topLocation);
        dest.writeTypedList(this.category);
    }

    public LocationList() {
    }

    protected LocationList(Parcel in) {
        this.topLocation = in.createTypedArrayList(LocationInfo.CREATOR);
        this.category = in.createTypedArrayList(Category.CREATOR);
    }

    public static final Creator<LocationList> CREATOR = new Creator<LocationList>() {
        @Override
        public LocationList createFromParcel(Parcel source) {
            return new LocationList(source);
        }

        @Override
        public LocationList[] newArray(int size) {
            return new LocationList[size];
        }
    };
}
