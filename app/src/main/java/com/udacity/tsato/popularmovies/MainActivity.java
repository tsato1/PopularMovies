package com.udacity.tsato.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    public static final String URL_END_POINT = "https://api.themoviedb.org/3";
    public static final String URL_IMG_BASE = "http://image.tmdb.org/t/p/";
    public static final String FUNC_MOST_POPLR = "/discover/movie?sort_by=popularity.desc";
    public static final String FUNC_HIGH_RATED = "/discover/movie/?certification_country=US&sort_by=vote_average.desc";
    public static final String API_KEY = "???";
    public static final String JSON_ENTRY_RESULTS = "results";
    public static final String JSON_ENTRY_TITLE = "original_title";
    public static final String JSON_ENTRY_POSTER_PATH = "poster_path";
    public static final String JSON_ENTRY_OVERVIEW = "overview";
    public static final String JSON_ENTRY_RELEASE_DATE = "release_date";
    public static final String JSON_ENTRY_VOTE_AVERAGE = "vote_average";

    static int screen_width;
    static int screen_height;
    static String url;

    private TextView mConnectivityTextView = null;
    private ProgressBar mProgressBar = null;
    private GridView mThumbnailsGridView = null;
    private GridAdapter mGridAdapter = null;
    private List<MovieItem> mMovielList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mConnectivityTextView = (TextView) findViewById(R.id.txv_no_connectivity);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mThumbnailsGridView = (GridView) findViewById(R.id.grv_thumbnails);
        mGridAdapter = new GridAdapter(getApplicationContext(), R.layout.grid_items, mMovielList);
        mThumbnailsGridView.setAdapter(mGridAdapter);
        mThumbnailsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                MovieItem item = (MovieItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra("itemSerializable", item);
                startActivity(intent);
            }
        });

        url = URL_END_POINT + FUNC_MOST_POPLR + API_KEY;
        fetchData();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    public void fetchData() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screen_width = metrics.widthPixels;
        screen_height = metrics.heightPixels;

        new HTTPAsyncTask(MainActivity.this, mMovielList, mGridAdapter, mConnectivityTextView, mProgressBar).execute(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            url = URL_END_POINT + FUNC_MOST_POPLR + API_KEY;
            fetchData();
            return true;
        } else if (id == R.id.action_top_rated) {
            url = URL_END_POINT + FUNC_HIGH_RATED + API_KEY;
            fetchData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
