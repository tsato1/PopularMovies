package com.udacity.tsato.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by T on 2016/01/19.
 */
public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private int mLayoutId;
    private List<String> mImageList;

    public GridAdapter(Context context, int layoutId, List<String> imageList){
        super();
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutId = layoutId;
        mImageList = imageList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String pathName = mImageList.get(position);

        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutId, parent, false);
        }

        Bitmap bitmap = BitmapFactory.decodeFile(pathName);

        return convertView;
    }

    public int getCount() {
        return mImageList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
}
