package com.scausum.okhttpsample;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by xin on 17/2/23.
 */

public class Category implements Parcelable {

    public String category;
    public List<LocationInfo> location;

    @Override
    public String toString() {
        return "Category{" +
                "category='" + category + '\'' +
                ", location=" + location +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.category);
        dest.writeTypedList(this.location);
    }

    public Category() {
    }

    protected Category(Parcel in) {
        this.category = in.readString();
        this.location = in.createTypedArrayList(LocationInfo.CREATOR);
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
