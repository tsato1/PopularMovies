package com.udacity.tsato.popularmovies;

import android.net.Uri;
import android.provider.BaseColumns;

public class DBColumns implements BaseColumns {
    public static final String PATH = "favorite";
    public static final Uri CONTENT_URI = Uri.parse("content://" + DBContentProvider.AUTHORITY + "/" + PATH);
    public static final String CONTENT_TYPE = "vnd.android.cursor.item/vnd.udacity.tsato.favorite";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.dir/vnd.udacity.tsato.favorite";

    public static final String TABLE = "items";

    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "_title";
    public static final String COL_POSTER_PATH = "_poster_path";
    public static final String COL_POSTER = "_poster";
    public static final String COL_OVERVIEW = "_overview";
    public static final String COL_RELEASE_DATE = "_release_date";
    public static final String COL_VOTE_AVERAGE ="_vote_average";
    public static final String COL_REVIEWS = "_reviews";
    public static final String COL_VIDEOS = "_videos";

    private DBColumns() {}
}
