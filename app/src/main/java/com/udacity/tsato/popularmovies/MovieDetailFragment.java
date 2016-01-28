package com.udacity.tsato.popularmovies;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

        if (movieItem == null) {
            mMovieDetailLinearLayout.setVisibility(View.GONE);
            mNothingSelectedTextView.setVisibility(View.VISIBLE);
        } else {
            mMovieDetailLinearLayout.setVisibility(View.VISIBLE);
            mNothingSelectedTextView.setVisibility(View.GONE);

            Picasso.with(getActivity()).load(MainActivity.URL_IMG_BASE + "w500/" + movieItem.posterPath).into(mPosterImageView);
            mTitleTextView.setText(movieItem.title);
            mReleaseDateTextView.setText(movieItem.releaseDate);
            mVoteAverageTextView.setText(movieItem.voteAverage);
            mSynopsisTextView.setText(movieItem.synopsis);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            mMovieItem = savedInstanceState.getParcelable("item");
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putParcelable("item", mMovieItem);
//    }
}
