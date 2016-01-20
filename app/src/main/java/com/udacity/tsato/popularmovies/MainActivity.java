package com.udacity.tsato.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private final String URL_END_POINT = "https://api.themoviedb.org/3";
    private final String FUNC_POPULAR = "/discover/movie?sort_by=popularity.desc";
    private final String FUNC_HIGH_RATED = "/discover/movie/?certification_country=US&sort_by=vote_average.desc";
    private final String API_KEY = "&api_key=0cba6b4bacdd159b108b2d7bc6ffe72b";

    private GridView mThumbnailsGridView;
    private List<String> mThumbnailList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mThumbnailsGridView = (GridView) findViewById(R.id.grv_thumbnails);
        GridAdapter gridAdapter = new GridAdapter(getApplicationContext(), R.layout.grid_items, mThumbnailList);
        mThumbnailsGridView.setAdapter(gridAdapter);

        new HTTPAsyncTask().execute(URL_END_POINT + FUNC_POPULAR + API_KEY);

        //ImageView testImage = (ImageView) findViewById(R.id.imv_test);
        //Picasso.with(this).load("http://i.imgur.com/2QYnI8T.jpg").into(testImage);
    }

    public class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        public String doInBackground(String... params) {
            String result = null;

            Request request = new Request.Builder()
                    .url(params[0])
                    .get()
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();

            try {
                Response response = okHttpClient.newCall(request).execute();
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        public void onPostExecute(String result) {
            //Log.d(TAG, result);

            JSONObject jsonObject = null;
            JSONArray jsonArray = null;

            try {
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject j = jsonArray.getJSONObject(i);
                    //Log.d(TAG, j.getString("poster_path"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
            new HTTPAsyncTask().execute(URL_END_POINT + FUNC_POPULAR + API_KEY);
            return true;
        } else if (id == R.id.action_top_rated) {
            new HTTPAsyncTask().execute(URL_END_POINT + FUNC_HIGH_RATED + API_KEY);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
