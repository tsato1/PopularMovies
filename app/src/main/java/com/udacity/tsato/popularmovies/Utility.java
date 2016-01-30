package com.udacity.tsato.popularmovies;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Utility {
    /** got from https://kennethflynn.wordpress.com/2012/09/12/putting-android-listviews-in-scrollviews/ **/
    public static void setListViewHeightBasedOnChildren(ReviewListAdapter adapter, ListView listView) {
        if (adapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
