package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class which will encapsulate all the data about a specific transaction in SelectCategory class
 * - implements Parcelable interface to move the data between activities
 */

public class Element implements Parcelable {
    public final String name;
    private int imgSrc;

    public Element(String name, int imgSrc) {
        this.name = name;
        this.imgSrc = imgSrc;
    }

    public int getImgSrc() {
        return imgSrc;
    }

    public String getName() {
        return name;
    }

    protected Element(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Element> CREATOR = new Creator<Element>() {
        @Override
        public Element createFromParcel(Parcel in) {
            return new Element(in);
        }

        @Override
        public Element[] newArray(int size) {
            return new Element[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
