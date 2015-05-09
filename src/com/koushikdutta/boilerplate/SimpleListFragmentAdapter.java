package com.koushikdutta.boilerplate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by koush on 3/29/15.
 */
public class SimpleListFragmentAdapter extends RecyclerView.Adapter<SimpleListFragmentAdapter.IconListViewHolder> {
    ArrayList<SimpleListItem> items = new ArrayList<SimpleListItem>();
    SimpleListFragment fragment;
    boolean selectable = true;
    View lastSelected;

    public class IconListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SimpleListItem item;
        public IconListViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            if (selectable) {
                if (lastSelected != null)
                    lastSelected.setSelected(false);
                v.setSelected(true);
                lastSelected = v;
            }
            item.onClick(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    @Override
    public IconListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        IconListViewHolder vh = new IconListViewHolder(view);
        view.setOnClickListener(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(IconListViewHolder holder, int position) {
        holder.item = items.get(position);
        holder.item.bindView(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public SimpleListFragmentAdapter(SimpleListFragment fragment) {
        this.fragment = fragment;
    }

    public SimpleListItem getItem(int i) {
        return items.get(i);
    }

    public SimpleListFragmentAdapter add(SimpleListItem item) {
        items.add(item);
        notifyDataSetChanged();
        return this;
    }

    public SimpleListFragmentAdapter remove(SimpleListItem item) {
        items.remove(item);
        notifyDataSetChanged();
        return this;
    }

    public SimpleListFragmentAdapter clear() {
        items.clear();
        notifyDataSetChanged();
        return this;
    }

    public SimpleListFragmentAdapter insert(SimpleListItem item, int index) {
        items.add(index, item);
        notifyDataSetChanged();
        return this;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
        if (!selectable && lastSelected != null) {
            lastSelected.setSelected(false);
            lastSelected = null;
        }
    }
}
