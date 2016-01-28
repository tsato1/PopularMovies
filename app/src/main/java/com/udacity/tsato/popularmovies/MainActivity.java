package com.udacity.tsato.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    static final String URL_END_POINT = "https://api.themoviedb.org/3";
    static final String URL_IMG_BASE = "http://image.tmdb.org/t/p/";
    static final String FUNC_MOST_POPLR = "/discover/movie?sort_by=popularity.desc";
    static final String FUNC_HIGH_RATED = "/discover/movie/?certification_country=US&sort_by=vote_average.desc";
    static final String API_KEY = "";
    static final String JSON_ENTRY_RESULTS = "results";
    static final String JSON_ENTRY_TITLE = "original_title";
    static final String JSON_ENTRY_POSTER_PATH = "poster_path";
    static final String JSON_ENTRY_OVERVIEW = "overview";
    static final String JSON_ENTRY_RELEASE_DATE = "release_date";
    static final String JSON_ENTRY_VOTE_AVERAGE = "vote_average";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
