package com.udacity.tsato.popularmovies;

import android.net.Uri;
import android.provider.BaseColumns;

public class DBColumns implements BaseColumns {
    public static final String TABLE_MOVIES = "table_movies";
    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "_title";
    public static final String COL_POSTER_PATH = "_poster_path";
    public static final String COL_POSTER = "_poster";
    public static final String COL_OVERVIEW = "_overview";
    public static final String COL_RELEASE_DATE = "_release_date";
    public static final String COL_VOTE_AVERAGE ="_vote_average";

    public static final String TABLE_REVIEWS = "table_reviews";
    public static final String COL_AUTHOR = "_author";
    public static final String COL_REVIEW = "_review";

    public static final String TABLE_VIDEOS = "table_videos";
    public static final String COL_NAME = "_name";
    public static final String COL_VIDEO = "_video";

    private DBColumns() {}
}
