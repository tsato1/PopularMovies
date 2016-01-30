package com.udacity.tsato.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.squareup.leakcanary.LeakCanary;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    static final String API_KEY = "";
    static final String URL_MOVIES_END_POINT = "https://api.themoviedb.org/3";
    static final String URL_MOVIE_END_POINT = "https://api.themoviedb.org/3/movie";
    static final String FUNC_MOST_POPLR = /* URL_MOVIES_END_POINT + */ "/discover/movie?sort_by=popularity.desc&api_key=" + API_KEY;
    static final String FUNC_HIGH_RATED = /* URL_MOVIES_END_POINT + */ "/discover/movie?certification_country=US&sort_by=vote_average.desc&api_key=" + API_KEY;
    static final String FUNC_REVIEWS = /* URL_MOVIE_END_POINT + id */ "/reviews?api_key=" + API_KEY;
    static final String FUNC_VIDEOS = /* URL_MOVIE_ END_POINT + id */ "/videos?api_key=" + API_KEY;
    static final String URL_IMG_BASE = "https://image.tmdb.org/t/p/" /* + size + poster_path */;

    static final String JSON_ENTRY_RESULTS = "results";
    static final String JSON_ENTRY_TITLE = "original_title";
    static final String JSON_ENTRY_POSTER_PATH = "poster_path";
    static final String JSON_ENTRY_OVERVIEW = "overview";
    static final String JSON_ENTRY_RELEASE_DATE = "release_date";
    static final String JSON_ENTRY_VOTE_AVERAGE = "vote_average";
    static final String JSON_ENTRY_ID = "id";
    static final String JSON_ENTRY_AUTHOR = "author";
    static final String JSON_ENTRY_CONTENT = "content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LeakCanary.install(getApplication());
    }
}
