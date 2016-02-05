package com.udacity.tsato.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class TrailerItem implements Parcelable {
    String trailer;
    String name;

    public TrailerItem (String trailer, String name) {
        this.trailer = trailer;
        this.name = name;
    }

    public TrailerItem() {
        super();
    }

    public TrailerItem(Parcel in) {
        this();
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.name = in.readString();
        this.trailer = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<TrailerItem> CREATOR = new Parcelable.Creator<TrailerItem> () {
        public TrailerItem createFromParcel(Parcel in) {
            return new TrailerItem(in);
        }

        public TrailerItem[] newArray(int size) {
            return new TrailerItem[size];
        }
    };

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(trailer);
    }
}
