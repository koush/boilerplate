package com.koushikdutta.boilerplate;

import android.view.View;
import android.widget.AbsListView;

/**
 * Created by koush on 4/4/15.
 */
public interface HeaderAbsListView {
    void addHeaderView(View view);
    void setOnScrollListener(AbsListView.OnScrollListener l);
}
