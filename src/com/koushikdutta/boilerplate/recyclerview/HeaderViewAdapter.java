package com.koushikdutta.boilerplate.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by koush on 4/19/15.
 */
public class HeaderViewAdapter extends RecyclerView.Adapter<HeaderViewAdapter.ViewHolder> {
    private static class Header {
        View view;
        int viewType;
    }
    ArrayList<Header> headers = new ArrayList<Header>();
    int viewTypeCount;

    public void addHeaderView(View header) {
        Header h = new Header();
        h.view = header;
        h.viewType = viewTypeCount++;
        headers.add(h);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return headers.get(position).viewType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (Header h: headers) {
            if (h.viewType == viewType)
                return new ViewHolder(h.view);
        }
        throw new AssertionError("unexpected viewtype");
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return headers.size();
    }
}
