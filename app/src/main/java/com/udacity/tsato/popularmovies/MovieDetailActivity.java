package com.udacity.tsato.popularmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {
    private ImageView mPosterImageView;
    private TextView mTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mSynopsisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle args = getIntent().getExtras();
        MovieItem item = (MovieItem) args.getParcelable("item");

        mPosterImageView = (ImageView) findViewById(R.id.imv_poster);
        Picasso.with(getApplicationContext()).load(MainActivity.URL_IMG_BASE + "w500/" + item.posterPath).into(mPosterImageView);
        mTitleTextView = (TextView) findViewById(R.id.txv_title);
        mTitleTextView.setText(item.title);
        mReleaseDateTextView = (TextView) findViewById(R.id.txv_release_date);
        mReleaseDateTextView.setText(item.releaseDate);
        mVoteAverageTextView = (TextView) findViewById(R.id.txv_vote_average);
        mVoteAverageTextView.setText(item.voteAverage);
        mSynopsisTextView = (TextView) findViewById(R.id.txv_synopsis);
        mSynopsisTextView.setText(item.synopsis);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
