package com.udacity.tsato.popularmovies;

import android.graphics.Bitmap;

import java.io.Serializable;

public class MovieItem implements Serializable {
    Bitmap poster;
    String posterPath;
    String title;
    String releaseDate;
    String voteAverage;
    String synopsis;

    public MovieItem(Bitmap poster, String posterPath, String title, String releaseDate, String voteAverage, String synopsis) {
        this.poster = poster;
        this.posterPath = posterPath;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.synopsis = synopsis;
    }
}
