package com.udacity.tsato.popularmovies;

import android.graphics.Bitmap;

import java.io.Serializable;

public class MovieItem implements Serializable {
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
}
