package com.koushikdutta.boilerplate.recyclerview;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by koush on 4/4/15.
 */
public interface IHeaderRecyclerView {
    void addHeaderView(int index, View view);
    void addOnScrollListener(RecyclerView.OnScrollListener l);
    int findFirstVisibleItemPosition();
}
