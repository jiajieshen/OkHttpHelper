package com.scausum.okhttpsample;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xin on 17/2/23.
 */

public class LocationInfo implements Parcelable {

    public String name;
    public String location;
    public String lat;
    public String lng;

    @Override
    public String toString() {
        return "LocationInfo{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    public String category;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.location);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
        dest.writeString(this.category);
    }

    public LocationInfo() {
    }

    protected LocationInfo(Parcel in) {
        this.name = in.readString();
        this.location = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
        this.category = in.readString();
    }

    public static final Creator<LocationInfo> CREATOR = new Creator<LocationInfo>() {
        @Override
        public LocationInfo createFromParcel(Parcel source) {
            return new LocationInfo(source);
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };
}
