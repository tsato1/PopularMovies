package com.udacity.tsato.popularmovies;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class MovieItem implements Parcelable {
    String posterPath;
    String title;
    String releaseDate;
    String voteAverage;
    String synopsis;

    public MovieItem(String posterPath, String title, String releaseDate, String voteAverage, String synopsis) {
        this.posterPath = posterPath;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.synopsis = synopsis;
    }

    private MovieItem(Parcel in) {
        this.posterPath = in.readString();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.voteAverage = in.readString();
        this.synopsis = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(posterPath);
        out.writeString(title);
        out.writeString(releaseDate);
        out.writeString(voteAverage);
        out.writeString(synopsis);
    }
}
