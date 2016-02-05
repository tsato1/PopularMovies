package com.udacity.tsato.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
    public final static int CODE_MOST_POPULAR = 0;
    public final static int CODE_HIGH_RATED = 1;
    public final static int CODE_FAVORITE = 2;

    private static String url = MainActivity.URL_MOVIES_END_POINT + MainActivity.FUNC_MOST_POPLR;

    private boolean mIsDualPane;
    private int pageCode;
    private MovieItem mMovieItem;
    private MovieGridAdapter mMovieGridAdapter = null;
    private List<MovieItem> mMovieList = new ArrayList<>();

    @Bind(R.id.txv_no_connectivity)
    TextView mConnectivityTextView;
    @Bind(R.id.progress)
    ProgressBar mProgressBar;
    @Bind(R.id.grv_thumbnails)
    GridView mThumbnailsGridView;

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

        mMovieGridAdapter = new MovieGridAdapter(getActivity(), R.layout.item_movie_grid, mMovieList);
        mThumbnailsGridView.setAdapter(mMovieGridAdapter);

        View movieDetailView = getActivity().findViewById(R.id.frag_movie_detail);
        mIsDualPane = movieDetailView != null && movieDetailView.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            mMovieItem = savedInstanceState.getParcelable("item");
            url = savedInstanceState.getString("url");
            pageCode = savedInstanceState.getInt("isFavoriteShown");
        }

        if (pageCode == CODE_FAVORITE) loadFromDatabase();
        else fetchFromCloud();

        if (mIsDualPane) {
            showMovieDetail(null);
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
            if (pageCode == CODE_FAVORITE) movieItem.poster = null;
            intent.putExtra("item", movieItem);
            intent.putExtra("indexOfItem", mMovieList.indexOf(movieItem));
            intent.putExtra("pageCode", pageCode);
            startActivityForResult(intent, pageCode);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("item", mMovieItem);
        outState.putString("url", url);
        outState.putInt("isFavoriteShown", pageCode);
    }

    private void fetchFromCloud() {
        new GetMainInfoAsync(getActivity(), mMovieList, mMovieGridAdapter, mConnectivityTextView, mProgressBar).execute(url);
    }

    private void loadFromDatabase() {
        mMovieList.clear();
        Cursor c = getActivity().getContentResolver().query(DBContentProvider.Movie.TABLE_MOVIES.contentUri, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                MovieItem item = new MovieItem(
                        c.getInt(c.getColumnIndex(DBColumns.COL_ID)),
                        c.getString(c.getColumnIndex(DBColumns.COL_POSTER)),
                        c.getString(c.getColumnIndex(DBColumns.COL_TITLE)),
                        c.getString(c.getColumnIndex(DBColumns.COL_RELEASE_DATE)),
                        c.getString(c.getColumnIndex(DBColumns.COL_VOTE_AVERAGE)),
                        c.getString(c.getColumnIndex(DBColumns.COL_OVERVIEW)),
                        c.getInt(c.getColumnIndex(DBColumns.COL_VIDEO))
                );
                Log.d(getClass().getSimpleName(), "movieItem.data_id = " + item.data_id + " : " + item.title);
                mMovieList.add(item);
            } while (c.moveToNext());
        }
        c.close();
        mMovieGridAdapter.notifyDataSetChanged();

        if (mMovieList.isEmpty()) mConnectivityTextView.setVisibility(View.VISIBLE);
        else mConnectivityTextView.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.action_popular) {
            url = MainActivity.URL_MOVIES_END_POINT + MainActivity.FUNC_MOST_POPLR;
            fetchFromCloud();
            pageCode = CODE_MOST_POPULAR;
            mConnectivityTextView.setText(R.string.error_connectivity_needed);
            return true;
        } else if (id == R.id.action_top_rated) {
            url = MainActivity.URL_MOVIES_END_POINT + MainActivity.FUNC_HIGH_RATED;
            fetchFromCloud();
            pageCode = CODE_HIGH_RATED;
            mConnectivityTextView.setText(R.string.error_connectivity_needed);
            return true;
        } else if (id == R.id.action_favorites) {
            loadFromDatabase();
            pageCode = CODE_FAVORITE;
            mConnectivityTextView.setText(R.string.info_no_favorites);
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_MOST_POPULAR:
                url = MainActivity.URL_MOVIES_END_POINT + MainActivity.FUNC_MOST_POPLR;
                fetchFromCloud();
                pageCode = CODE_MOST_POPULAR;
                mConnectivityTextView.setText(R.string.error_connectivity_needed);
                break;
            case CODE_HIGH_RATED:
                url = MainActivity.URL_MOVIES_END_POINT + MainActivity.FUNC_HIGH_RATED;
                fetchFromCloud();
                pageCode = CODE_HIGH_RATED;
                mConnectivityTextView.setText(R.string.error_connectivity_needed);
                break;
            case CODE_FAVORITE:
                loadFromDatabase();
                pageCode = CODE_FAVORITE;
                mConnectivityTextView.setText(R.string.info_no_favorites);
                break;
        }
    }
}
