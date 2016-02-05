package com.udacity.tsato.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewItem implements Parcelable {
    String author;
    String content;

    public ReviewItem (String author, String content) {
        this.author = author;
        this.content = content;
    }

    public ReviewItem () {
        super();
    }

    public ReviewItem (Parcel in) {
        this();
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
    }

    public int describeContents () {
        return 0;
    }

    public static final Parcelable.Creator<ReviewItem> CREATOR = new Parcelable.Creator<ReviewItem>() {
        public ReviewItem createFromParcel(Parcel in) {
            return new ReviewItem(in);
        }

        public ReviewItem[] newArray(int size) {
            return new ReviewItem[size];
        }
    };

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(author);
        out.writeString(content);
    }
}
