package com.koushikdutta.boilerplate.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.koushikdutta.boilerplate.HeaderRecyclerView;

import java.util.Hashtable;

/**
 * Created by koush on 4/19/15.
 */
public class GridRecyclerView extends RecyclerView implements HeaderRecyclerView {
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

    public interface SpanningViewHolder {
        int getSpanSize(int spanCount);
    }

    public void setNumColumns(int numColumns) {
        setNumColumns(getContext(), numColumns);
    }

    Hashtable<Integer, Integer> typeToSpan = new Hashtable<Integer, Integer>();
    private void setNumColumns(Context context, int numColumns) {
        if (gridLayoutManager == null) {
            gridLayoutManager = new GridLayoutManager(context, numColumns);
            gridLayoutManager.setSpanSizeLookup(spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = adapterWrapper.getItemViewType(position);
                    Integer span = typeToSpan.get(viewType);
                    if (span != null)
                        return span;
                    ViewHolder vh = adapterWrapper.createViewHolder(GridRecyclerView.this, viewType);
                    int foundSpan;
                    if (vh instanceof SpanningViewHolder)
                        foundSpan = ((SpanningViewHolder)vh).getSpanSize(gridLayoutManager.getSpanCount());
                    else
                        foundSpan = 1;
                    typeToSpan.put(viewType, foundSpan);
                    return foundSpan;
                }
            });
            setLayoutManager(gridLayoutManager);
        }
        else {
            gridLayoutManager.setSpanCount(numColumns);
        }
        typeToSpan.clear();
        spanSizeLookup.invalidateSpanIndexCache();
        requestLayout();
    }

    void updateEmptyState() {
        if (emptyView == null)
            return;
        if (adapterWrapper.getItemCount() - headerViewAdapter.getItemCount() == 0) {
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
    AdapterWrapper.WrappedAdapter wrappedAdapter;

    @Override
    public void setAdapter(Adapter adapter) {
        adapterWrapper.remove(this.adapter);
        this.adapter = adapter;
        wrappedAdapter = adapterWrapper.wrapAdapter(adapter);
        super.setAdapter(adapterWrapper);
    }

    public AdapterWrapper.WrappedAdapter getWrappedAdapter() {
        return wrappedAdapter;
    }

    public void addHeaderView(View view) {
        headerViewAdapter.addHeaderView(headerViewAdapter.getItemCount(), view);
    }

    @Override
    public void addHeaderView(int index, View view) {
        headerViewAdapter.addHeaderView(index, view);
    }

    @Override
    public int findFirstVisibleItemPosition() {
        return gridLayoutManager.findFirstVisibleItemPosition();
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
