package com.koushikdutta.boilerplate;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ScrollView;

/**
 * Created by koush on 4/4/15.
 */
public interface HeaderRecyclerView {
    void addHeaderView(int index, View view);
    void addOnScrollListener(RecyclerView.OnScrollListener l);
    int findFirstVisibleItemPosition();
}
