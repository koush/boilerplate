package com.koushikdutta.boilerplate.recyclerview;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by koush on 5/24/15.
 */
public class HeaderRecyclerView extends RecyclerView implements IHeaderRecyclerView {
    private Adapter adapter;
    private AdapterWrapper adapterWrapper = new AdapterWrapper();
    private HeaderViewAdapter headerViewAdapter = new HeaderViewAdapter();
    private AdapterWrapper.WrappedAdapter wrappedAdapter;
    private View emptyView;
    public HeaderRecyclerView(Context context) {
        super(context);
        init(context, null, 0);
    }
    public HeaderRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }
    public HeaderRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    void init(Context context, AttributeSet attrs, int defStyleAttr) {
        adapterWrapper.wrapAdapter(headerViewAdapter);
        adapterWrapper.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                updateEmptyState();
            }
        });
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
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
        return ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
    }

    public void setEmptyView(View view) {
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
            setVisibility(View.VISIBLE);
        }
        emptyView = view;
        updateEmptyState();
    }

    void updateEmptyState() {
        if (emptyView == null)
            return;
        if (adapterWrapper.getItemCount() - headerViewAdapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            setVisibility(View.VISIBLE);
        }
    }
}
