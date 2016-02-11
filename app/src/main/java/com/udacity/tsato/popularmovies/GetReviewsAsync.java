package com.udacity.tsato.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetReviewsAsync extends AsyncTask<String, Void, String> {
    private final static String TAG = "GetReviewsAsync";

    private Context mContext;
    private List<ReviewItem> mReviewList;
    private ReviewListAdapter mListAdapter;
    private ListView mListView;
    private ProgressBar mProgressBar;

    public GetReviewsAsync(Context context, List<ReviewItem> reviews, ReviewListAdapter listAdapter, ListView listView, ProgressBar progressBar) {
        mContext = context;
        mReviewList = reviews;
        mListAdapter = listAdapter;
        mListView = listView;
        mProgressBar = progressBar;
    }

    @Override
    public void onPreExecute() {
        showProgress(true);
    }

    @Override
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

    @Override
    public void onPostExecute(String result) {
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;

        if (result != null) {
            try {
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray(MainActivity.JSON_ENTRY_RESULTS);
                mReviewList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject j = jsonArray.getJSONObject(i);
                    ReviewItem item = new ReviewItem(
                            j.getString(MainActivity.JSON_ENTRY_AUTHOR).trim(),
                            j.getString(MainActivity.JSON_ENTRY_CONTENT.trim())
                    );
                    mReviewList.add(item);
                    Log.d(TAG, item.author);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mListAdapter.notifyDataSetChanged();
        Utility.setListViewHeightBasedOnChildren(mListAdapter, mListView);
        showProgress(false);
    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
