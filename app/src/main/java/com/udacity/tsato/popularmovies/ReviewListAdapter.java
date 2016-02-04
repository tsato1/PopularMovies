package com.udacity.tsato.popularmovies;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class ReviewListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private int mLayoutId;
    private List<ReviewItem> mReviewList;

    public ReviewListAdapter(Context context, int layoutId, List<ReviewItem> reviewList) {
        super();
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutId = layoutId;
        mReviewList = reviewList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewItem item = mReviewList.get(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.authorTextView = (TextView) convertView.findViewById(R.id.txv_author);
            viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.txv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.authorTextView.setText(mContext.getString(R.string.author) + "  " + item.author);
        viewHolder.contentTextView.setText(item.content);

        return convertView;
    }

    public int getCount() {
        return mReviewList.size();
    }

    public Object getItem(int position) {
        return mReviewList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView authorTextView;
        TextView contentTextView;
    }
}
