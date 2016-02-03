package com.udacity.tsato.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetMainInfoAsync extends AsyncTask<String, Void, String> {
    private final String TAG = "GetMainInfoAsync";

    private Context mContext;
    private List<MovieItem> mMovielList = new ArrayList<>();
    private MovieGridAdapter mMovieGridAdapter;
    private TextView mTextView;
    private ProgressBar mProgressBar;

    public GetMainInfoAsync(Context context, List<MovieItem> list, MovieGridAdapter adapter, TextView textView, ProgressBar progressBar) {
        mContext = context;
        mMovielList = list;
        mMovieGridAdapter = adapter;
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
            }
            response.body().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void onPostExecute(String result) {
        //Log.d(TAG, result);

        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        mMovielList.clear();

        if (result == null) {
            mTextView.setVisibility(View.VISIBLE);
        } else {
            mTextView.setVisibility(View.GONE);

            try {
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray(MainActivity.JSON_ENTRY_RESULTS);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject j = jsonArray.getJSONObject(i);

                    MovieItem item = new MovieItem(
                            0,
                            j.getString(MainActivity.JSON_ENTRY_POSTER_PATH),
                            j.getString(MainActivity.JSON_ENTRY_TITLE),
                            j.getString(MainActivity.JSON_ENTRY_RELEASE_DATE),
                            j.getString(MainActivity.JSON_ENTRY_VOTE_AVERAGE),
                            j.getString(MainActivity.JSON_ENTRY_OVERVIEW),
                            j.getInt(MainActivity.JSON_ENTRY_ID)
                    );

                    mMovielList.add(item);
                    //Log.d(TAG, String.valueOf(j.getInt(MainActivity.JSON_ENTRY_ID)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mMovieGridAdapter.notifyDataSetChanged();
        showProgress(false);
    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show? View.VISIBLE: View.GONE);
    }
}

