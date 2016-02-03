package com.udacity.tsato.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieItem implements Parcelable {
    int data_id;
    String poster;
    String title;
    String releaseDate;
    String voteAverage;
    String synopsis;
    int movie_id;

    public MovieItem(int data_id, String poster, String title, String releaseDate, String voteAverage, String synopsis, int movie_id) {
        this.data_id = data_id;
        this.poster = poster; // stores poster base64 string for favorite: otherwise stores poster path to the tmdb cloud
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.synopsis = synopsis;
        this.movie_id = movie_id;
    }

    private MovieItem(Parcel in) {
        this.data_id = in.readInt();
        this.poster = in.readString();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.voteAverage = in.readString();
        this.synopsis = in.readString();
        this.movie_id = in.readInt();
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
        out.writeInt(data_id);
        out.writeString(poster);
        out.writeString(title);
        out.writeString(releaseDate);
        out.writeString(voteAverage);
        out.writeString(synopsis);
        out.writeInt(movie_id);
    }
}
