package com.udacity.tsato.popularmovies;

import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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

            Picasso.with(getActivity()).load(MainActivity.URL_IMG_BASE + "w342/" + movieItem.poster).into(mPosterImageView);
            mTitleTextView.setText(movieItem.title);
            mReleaseDateTextView.setText(movieItem.releaseDate);
            mVoteAverageTextView.setText(movieItem.voteAverage + "/10.0");
            mSynopsisTextView.setText(movieItem.synopsis);
            new GetTrailersAsync(getActivity(), mTrailerList, mTrailerListAdapter, mTrailerListView, mTrailerProgressBar).execute(MainActivity.URL_MOVIE_END_POINT + "/" + movieItem.movie_id + MainActivity.FUNC_VIDEOS);
            new GetReviewsAsync(getActivity(), mReviewList, mReviewListAdapter, mReviewListView, mReviewProgressBar).execute(MainActivity.URL_MOVIE_END_POINT + "/" + movieItem.movie_id + MainActivity.FUNC_REVIEWS);
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

        String encoded = Utility.convertImageViewToString(mPosterImageView);

        ContentValues values = new ContentValues();
        values.clear();
        values.put(DBColumns.COL_TITLE, movieItem.title);
        values.put(DBColumns.COL_POSTER_PATH, movieItem.poster);
        values.put(DBColumns.COL_POSTER, encoded);
        values.put(DBColumns.COL_OVERVIEW, movieItem.synopsis);
        values.put(DBColumns.COL_RELEASE_DATE, movieItem.releaseDate);
        values.put(DBColumns.COL_VOTE_AVERAGE, movieItem.voteAverage);
        String[] authors = new String[mReviewList.size()];
        String[] reviews = new String[mReviewList.size()];
        for (int i = 0; i < mReviewList.size(); i++) {
            authors[i] = mReviewList.get(i).author;
            reviews[i] = mReviewList.get(i).content;
        }
        values.put(DBColumns.COL_AUTHOR, Utility.convertArrayToString(authors));
        values.put(DBColumns.COL_REVIEW, Utility.convertArrayToString(reviews));
        String[] names = new String[mTrailerList.size()];
        String[] trailers = new String[mTrailerList.size()];
        for (int i = 0; i < mTrailerList.size(); i++) {
            names[i] = mTrailerList.get(i).name;
            trailers[i] = mTrailerList.get(i).trailer;
        }
        values.put(DBColumns.COL_NAME, Utility.convertArrayToString(names));
        values.put(DBColumns.COL_VIDEO, Utility.convertArrayToString(trailers));
        getActivity().getContentResolver().insert(DBContentProvider.Movie.TABLE_MOVIES.contentUri, values);

        Cursor c = getActivity().getContentResolver().query(DBContentProvider.Movie.TABLE_MOVIES.contentUri, null, null, null, null);
        //if (c.moveToLast()) mMovieItem.data_id = c.getInt(c.getColumnIndex(DBColumns.COL_ID));
//        Log.d(getClass().getSimpleName(), String.valueOf(mMovieItem.data_id));
//        if (c.moveToFirst()) {
//            do {
//                for (int i = 0; i < c.getColumnCount(); i++) {
//                    Log.d(getClass().getSimpleName(), c.getColumnName(i) + " : " + c.getString(i));
//                }
//            } while (c.moveToNext());
//        }
    }

    private void removeFromDB_mMovieItem() {
        if (mMovieItem == null) return;

        MovieItem movieItem = mMovieItem;
        getActivity().getContentResolver().delete(ContentUris.withAppendedId(DBContentProvider.Movie.TABLE_MOVIES.contentUri, movieItem.data_id), null, null);
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
                removeFromDB_mMovieItem();
            } else {
                saveAsFavorite_mMovieItem();
            }

            mFavoriteMenuItem.setVisible(mIsFavorite);
            mUnfavoriteMenuItem.setVisible(!mIsFavorite);
            mIsFavorite = !mIsFavorite;
            return true;
        } else if (id == R.id.action_share) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + mTrailerList.get(0).trailer);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
