package com.udacity.tsato.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class HTTPAsyncTask extends AsyncTask<String, Void, String> {
    private final String TAG = "HTTPAsyncTask";

    private static boolean isNetworkAvailable = false;

    private Context mContext;
    private List<MovieItem> mMovielList = new ArrayList<>();
    private GridAdapter mGridAdapter;
    private TextView mTextView;
    private ProgressBar mProgressBar;

    public HTTPAsyncTask(Context context, List<MovieItem> list, GridAdapter adapter, TextView textView, ProgressBar progressBar) {
        mContext = context;
        mMovielList = list;
        mGridAdapter = adapter;
        mTextView = textView;
        mProgressBar = progressBar;
    }

    public void onPreExecute() {
        showProgress(true);
    }

    public String doInBackground(String... params) {
        String result = null;

        Request request = new Request.Builder()
                .url(params[0])
                .get()
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.code() == 200) {
                result = response.body().string();
                isNetworkAvailable = true;
            }
            else isNetworkAvailable = false;
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

        if (result != null) {
            try {
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray(MainActivity.JSON_ENTRY_RESULTS);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject j = jsonArray.getJSONObject(i);


                    MovieItem item = new MovieItem(
                            j.getString(MainActivity.JSON_ENTRY_POSTER_PATH),
                            j.getString(MainActivity.JSON_ENTRY_TITLE),
                            j.getString(MainActivity.JSON_ENTRY_RELEASE_DATE),
                            j.getString(MainActivity.JSON_ENTRY_VOTE_AVERAGE),
                            j.getString(MainActivity.JSON_ENTRY_OVERVIEW)
                    );

                    mMovielList.add(item);
                    Log.d(TAG, j.getString(MainActivity.JSON_ENTRY_POSTER_PATH));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (isNetworkAvailable) mTextView.setVisibility(View.GONE);
        else mTextView.setVisibility(View.VISIBLE);

        mGridAdapter.notifyDataSetChanged();
        showProgress(false);
    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show? View.VISIBLE: View.GONE);
    }
}

