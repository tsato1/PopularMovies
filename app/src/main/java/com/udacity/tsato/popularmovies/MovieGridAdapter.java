package com.udacity.tsato.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieGridAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private int mLayoutId;
    private List<MovieItem> mMovieList;

    public MovieGridAdapter(Context context, int layoutId, List<MovieItem> movieList){
        super();
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutId = layoutId;
        mMovieList = movieList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MovieItem item = mMovieList.get(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.imv_thumbnail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (item.data_id == 0) {
            Picasso.with(mContext).load(MainActivity.URL_IMG_BASE + "w185/" + item.poster).into(viewHolder.thumbnailImageView);
        } else {
            viewHolder.thumbnailImageView.setImageBitmap(Utility.convertStringToBitmap(item.poster));
        }

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

    static class ViewHolder {
        ImageView thumbnailImageView;
    }
}
