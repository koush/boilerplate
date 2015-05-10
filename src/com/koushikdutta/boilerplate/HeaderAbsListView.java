package com.koushikdutta.boilerplate;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ScrollView;

/**
 * Created by koush on 4/4/15.
 */
public interface HeaderAbsListView {
    interface OnScrollListener {
        void onScrollStateChanged(ViewGroup view, int scrollState);
        void onScroll(ViewGroup view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount);
    }

    void addHeaderView(int index, View view);
    void setOnScrollListener(OnScrollListener l);
}
