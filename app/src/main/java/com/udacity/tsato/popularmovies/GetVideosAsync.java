package com.udacity.tsato.popularmovies;

import android.content.Context;
import android.os.AsyncTask;

public class GetVideosAsync extends AsyncTask<String, Void, String> {
    private Context mContext;

    public GetVideosAsync (Context context) {
        mContext = context;
    }

    @Override
    public String doInBackground(String... params) {
        return params[0];
    }

    @Override
    public void onPostExecute(String result) {

    }
}
