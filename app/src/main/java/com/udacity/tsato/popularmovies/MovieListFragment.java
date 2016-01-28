package com.udacity.tsato.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MovieListFragment extends Fragment {
    private static String url = MainActivity.URL_END_POINT + MainActivity.FUNC_MOST_POPLR + MainActivity.API_KEY;

    private boolean mIsDualPane;
    private MovieItem mMovieItem;
    private GridAdapter mGridAdapter = null;
    private List<MovieItem> mMovieList = new ArrayList<>();

    @Bind(R.id.txv_no_connectivity) TextView mConnectivityTextView;
    @Bind(R.id.progress) ProgressBar mProgressBar;
    @Bind(R.id.grv_thumbnails) GridView mThumbnailsGridView;
    @OnItemClick(R.id.grv_thumbnails)
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        showMovieDetail((MovieItem) parent.getItemAtPosition(position));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

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

        mGridAdapter = new GridAdapter(getActivity(), R.layout.grid_items, mMovieList);
        mThumbnailsGridView.setAdapter(mGridAdapter);
        fetchData();

        View movieDetailView = getActivity().findViewById(R.id.frag_movie_detail);
        mIsDualPane = movieDetailView != null && movieDetailView.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            mMovieItem = savedInstanceState.getParcelable("item");
            url = savedInstanceState.getString("url");
        }

        if (mIsDualPane) {
            showMovieDetail(mMovieList.get(0));
        }
    }

    private void showMovieDetail(MovieItem movieItem) {
        mMovieItem = movieItem;

        if (mIsDualPane) {
            MovieDetailFragment movieDetailFragment = (MovieDetailFragment) getFragmentManager().findFragmentById(R.id.frag_movie_detail);

            if (movieDetailFragment == null) {
                movieDetailFragment = MovieDetailFragment.newInstance();
                Bundle args = new Bundle();
                args.putParcelable("item", movieItem);
                movieDetailFragment.setArguments(args);

                getFragmentManager().beginTransaction()
                        .replace(R.id.frag_movie_detail, movieDetailFragment)
                        .commit();
            }
        } else {
            Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
            intent.putExtra("item", movieItem);
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("item", mMovieItem);
        outState.putString("url", url);
    }

    public void fetchData() {
        new HTTPAsyncTask(getActivity(), mMovieList, mGridAdapter, mConnectivityTextView, mProgressBar).execute(url);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.action_popular) {
            url = MainActivity.URL_END_POINT + MainActivity.FUNC_MOST_POPLR + MainActivity.API_KEY;
            fetchData();
            return true;
        } else if (id == R.id.action_top_rated) {
            url = MainActivity.URL_END_POINT + MainActivity.FUNC_HIGH_RATED + MainActivity.API_KEY;
            fetchData();
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
