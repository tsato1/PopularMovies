package com.udacity.tsato.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TrailerListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private int mLayoutId;
    private List<TrailerItem> mTrailerList;

    public TrailerListAdapter (Context context, int layoutId, List<TrailerItem> trailerList) {
        super();
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutId = layoutId;
        mTrailerList = trailerList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TrailerItem item = mTrailerList.get(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.trailerImageView = (ImageView) convertView.findViewById(R.id.imv_trailer);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.txv_name);
            viewHolder.trailerImageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //todo start a new intent
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.trailerImageView.setImageResource(R.mipmap.ic_launcher);
        viewHolder.nameTextView.setText(item.name);

        return convertView;
    }

    public int getCount() {
        return mTrailerList.size();
    }

    public Object getItem(int position) {
        return mTrailerList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView trailerImageView;
        TextView nameTextView;
    }
}
