package com.udacity.tsato.popularmovies;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailFragment extends Fragment {
    @Bind(R.id.txv_nothing_selected) TextView mNothingSelectedTextView;
    @Bind(R.id.lnl_movie_detail) LinearLayout mMovieDetailLinearLayout;
    @Bind(R.id.imv_poster) ImageView mPosterImageView;
    @Bind(R.id.txv_title) TextView mTitleTextView;
    @Bind(R.id.txv_release_date) TextView mReleaseDateTextView;
    @Bind(R.id.txv_vote_average) TextView mVoteAverageTextView;
    @Bind(R.id.txv_synopsis) TextView mSynopsisTextView;
    @Bind(R.id.lsv_trailers) ListView mTrailerListView;
    @Bind(R.id.lsv_reviews) ListView mReviewListView;
    @Bind(R.id.progress_trailers) ProgressBar mTrailerProgressBar;
    @Bind(R.id.progress_reviews) ProgressBar mReviewProgressBar;

    private MovieItem mMovieItem;
    private boolean mIsFavorite;
    private MenuItem mFavoriteMenuItem;
    private MenuItem mUnfavoriteMenuItem;

    private List<ReviewItem> mReviewList = new ArrayList<>();
    private ReviewListAdapter mReviewListAdapter = null;
    private List<TrailerItem> mTrailerList = new ArrayList<>();
    private TrailerListAdapter mTrailerListAdapter = null;

    public static MovieDetailFragment newInstance() {
        MovieDetailFragment fragment = new MovieDetailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) return null;

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, view);

        MovieItem movieItem = getArguments().getParcelable("item");

        mReviewListAdapter = new ReviewListAdapter(getActivity(), R.layout.item_review_list, mReviewList);
        mReviewListView.setAdapter(mReviewListAdapter);
        mTrailerListAdapter = new TrailerListAdapter(getActivity(), R.layout.item_trailer_list, mTrailerList);
        mTrailerListView.setAdapter(mTrailerListAdapter);

        if (movieItem == null) {
            mMovieDetailLinearLayout.setVisibility(View.GONE);
            mNothingSelectedTextView.setVisibility(View.VISIBLE);
        } else {
            mMovieDetailLinearLayout.setVisibility(View.VISIBLE);
            mNothingSelectedTextView.setVisibility(View.GONE);

            Picasso.with(getActivity()).load(MainActivity.URL_IMG_BASE + "w342/" + movieItem.posterPath).into(mPosterImageView);
            mTitleTextView.setText(movieItem.title);
            mReleaseDateTextView.setText(movieItem.releaseDate);
            mVoteAverageTextView.setText(movieItem.voteAverage + "/10.0");
            mSynopsisTextView.setText(movieItem.synopsis);
            new GetTrailersAsync(getActivity(), mTrailerList, mTrailerListAdapter, mTrailerListView, mTrailerProgressBar).execute(MainActivity.URL_MOVIE_END_POINT + "/" + movieItem.id + MainActivity.FUNC_VIDEOS);
            new GetReviewsAsync(getActivity(), mReviewList, mReviewListAdapter, mReviewListView, mReviewProgressBar).execute(MainActivity.URL_MOVIE_END_POINT + "/" + movieItem.id + MainActivity.FUNC_REVIEWS);
        }

        //todo check against db if item is favorite or not
        // set mIsFavorite = true|false;

        mMovieItem = movieItem;
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            mIsFavorite = savedInstanceState.getBoolean("isFavorite");

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isFavorite", mIsFavorite);
    }

    private void saveAsFavorite_mMovieItem() {
        MovieItem movieItem = mMovieItem;

        if (movieItem == null) return;

        /*** encoding poster imageview into string ***/
        ImageView imageView = new ImageView(getActivity());
        Picasso.with(getActivity()).load(MainActivity.URL_IMG_BASE + "w500/" + movieItem.posterPath).into(imageView);
        imageView.buildDrawingCache();
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        ContentValues values = new ContentValues();
        values.put(DBColumns.COL_TITLE, movieItem.title);
        values.put(DBColumns.COL_POSTER_PATH, movieItem.posterPath);
        values.put(DBColumns.COL_POSTER, encoded);
        values.put(DBColumns.COL_OVERVIEW, movieItem.synopsis);
        values.put(DBColumns.COL_RELEASE_DATE, movieItem.releaseDate);
        values.put(DBColumns.COL_VOTE_AVERAGE, movieItem.voteAverage);
        //values.put(DBColumns.COL_REVIEWS, mReviews);
        //values.put(DBColumns.COL_VIDEOS, mVideos);
    }

    private void removeFromDB_mMovieItem() {
        if (mMovieItem == null) return;

        MovieItem movieItem = mMovieItem;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        mFavoriteMenuItem = menu.findItem(R.id.action_favorite);
        mUnfavoriteMenuItem = menu.findItem(R.id.action_unfavorite);
        mFavoriteMenuItem.setVisible(!mIsFavorite);
        mUnfavoriteMenuItem.setVisible(mIsFavorite);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.action_favorite || id == R.id.action_unfavorite) {
            if (mIsFavorite) {
                //removeFromDB_mMovieItem();
            } else {
                //saveAsFavorite_mMovieItem();
            }

            mFavoriteMenuItem.setVisible(mIsFavorite);
            mUnfavoriteMenuItem.setVisible(!mIsFavorite);
            mIsFavorite = !mIsFavorite;
            return true;
        } else if (id == R.id.action_share) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "test");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
