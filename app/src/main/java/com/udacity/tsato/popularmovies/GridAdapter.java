package com.udacity.tsato.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private int mLayoutId;
    private List<MovieItem> mMovieList;

    public GridAdapter(Context context, int layoutId, List<MovieItem> movieList){
        super();
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutId = layoutId;
        mMovieList = movieList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MovieItem item = mMovieList.get(position);

        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutId, parent, false);
            convertView.setMinimumWidth(MainActivity.screen_width/2);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imv_thumbnail);
        //imageView.setImageBitmap(item.getPoster());
        Picasso.with(mContext).load(MainActivity.URL_IMG_BASE + "w500/" + item.posterPath).into(imageView);

        return convertView;
    }

    public int getCount() {
        return mMovieList.size();
    }

    public Object getItem(int position) {
        return mMovieList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
}
