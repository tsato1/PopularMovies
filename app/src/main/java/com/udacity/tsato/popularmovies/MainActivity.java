package com.udacity.tsato.popularmovies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    public static final String URL_END_POINT = "https://api.themoviedb.org/3";
    public static final String URL_IMG_BASE = "http://image.tmdb.org/t/p/";
    public static final String FUNC_MOST_POPLR = "/discover/movie?sort_by=popularity.desc";
    public static final String FUNC_HIGH_RATED = "/discover/movie/?certification_country=US&sort_by=vote_average.desc";
    public static final String API_KEY = "&api_key=0cba6b4bacdd159b108b2d7bc6ffe72b";
    public static final String JSON_ENTRY_RESULTS = "results";
    public static final String JSON_ENTRY_TITLE = "original_title";
    public static final String JSON_ENTRY_POSTER_PATH = "poster_path";
    public static final String JSON_ENTRY_OVERVIEW = "overview";
    public static final String JSON_ENTRY_RELEASE_DATE = "release_date";
    public static final String JSON_ENTRY_VOTE_AVERAGE = "vote_average";

    public static int screen_width;
    public static int screen_height;
    public static String url;

    private GridView mThumbnailsGridView = null;
    private GridAdapter mGridAdapter = null;
    private List<MovieItem> mMovielList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        url = URL_END_POINT + FUNC_MOST_POPLR + API_KEY;
        fetchData();
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())){
                return true;
            }
            else return false;
        } else return false;
    }

    private void fetchData() {
        if (!isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.error_no_internet);
            builder.setMessage(R.string.error_please_connect_internet);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        } else {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            screen_width = metrics.widthPixels;
            screen_height = metrics.heightPixels;

            new HTTPAsyncTask().execute(url);

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
        }
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

            mMovielList.clear();
            JSONObject jsonObject = null;
            JSONArray jsonArray = null;

            try {
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray(JSON_ENTRY_RESULTS);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject j = jsonArray.getJSONObject(i);

                    /*** todo want to store bitmap in MovieItem ***/
                    ImageView imageView = new ImageView(getApplicationContext());
                    Picasso.with(getApplicationContext()).load(MainActivity.URL_IMG_BASE + "w185/" + j.getString(JSON_ENTRY_POSTER_PATH)).into(imageView);
                    imageView.setDrawingCacheEnabled(true);
                    Bitmap bitmap = imageView.getDrawingCache();

                    MovieItem item = new MovieItem(
                            bitmap,
                            j.getString(JSON_ENTRY_POSTER_PATH),
                            j.getString(JSON_ENTRY_TITLE),
                            j.getString(JSON_ENTRY_RELEASE_DATE),
                            j.getString(JSON_ENTRY_VOTE_AVERAGE),
                            j.getString(JSON_ENTRY_OVERVIEW)
                    );

                    mMovielList.add(item);
                    //Log.d(TAG, j.getString(JSON_ENTRY_POSTER_PATH));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mGridAdapter.notifyDataSetChanged();
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
