package com.koushikdutta.boilerplate.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.koushikdutta.boilerplate.HeaderAbsListView;

/**
 * Created by koush on 4/19/15.
 */
public class GridRecyclerView extends RecyclerView implements HeaderAbsListView {
    public GridRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public GridRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GridRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setNumColumns(int numColumns) {
        setNumColumns(getContext(), numColumns);
    }

    private void setNumColumns(Context context, int numColumns) {
        if (gridLayoutManager == null) {
            gridLayoutManager = new GridLayoutManager(context, numColumns);
            gridLayoutManager.setSpanSizeLookup(spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position < headerViewAdapter.getItemCount())
                        return gridLayoutManager.getSpanCount();
                    if (adapterWrapper.isEmptyView(position))
                        return gridLayoutManager.getSpanCount();
                    return 1;
                }
            });
            setLayoutManager(gridLayoutManager);
        }
        else {
            gridLayoutManager.setSpanCount(numColumns);
        }
        spanSizeLookup.invalidateSpanIndexCache();
        requestLayout();
    }

    void updateEmptyState() {
        if (emptyView == null)
            return;
        if (adapterWrapper.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            setVisibility(View.GONE);
        }
        else {
            emptyView.setVisibility(View.GONE);
            setVisibility(View.VISIBLE);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        setHasFixedSize(true);
        setNumColumns(context, 1);
        adapterWrapper.wrapAdapter(headerViewAdapter);
        adapterWrapper.registerAdapterDataObserver(new AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                updateEmptyState();
            }
        });
    }

    GridLayoutManager gridLayoutManager;
    Adapter adapter;
    AdapterWrapper adapterWrapper = new AdapterWrapper();
    HeaderViewAdapter headerViewAdapter = new HeaderViewAdapter();
    GridLayoutManager.SpanSizeLookup spanSizeLookup;

    @Override
    public void setAdapter(Adapter adapter) {
        adapterWrapper.remove(this.adapter);
        this.adapter = adapter;
        adapterWrapper.wrapAdapter(adapter);
        super.setAdapter(adapterWrapper);
    }

    @Override
    public void addHeaderView(View view) {
        headerViewAdapter.addHeaderView(view);
    }

    @Override
    public void setOnScrollListener(final HeaderAbsListView.OnScrollListener l) {
        super.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                l.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int first = gridLayoutManager.findFirstVisibleItemPosition();
                int last = gridLayoutManager.findLastVisibleItemPosition();
                l.onScroll(recyclerView, first, last - first, adapter.getItemCount());
            }
        });
    }

    View emptyView;
    public void setEmptyView(View view) {
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
            setVisibility(View.VISIBLE);
        }
        emptyView = view;
        updateEmptyState();
    }
}
