package com.koushikdutta.boilerplate.simplelist;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koushikdutta.boilerplate.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by koush on 3/29/15.
 */
public class SimpleListFragmentAdapter extends RecyclerView.Adapter<SimpleListFragmentAdapter.IconListViewHolder> {
    ArrayList<SimpleListItem> items = new ArrayList<SimpleListItem>();
    Resources resources;
    boolean selectable = true;
    WeakReference<SimpleListItem> lastSelected;
    Comparator<SimpleListItem> sort;

    public void sort(Comparator<SimpleListItem> sort) {
        this.sort = sort;
    }

    public class IconListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        SimpleListItem item;
        public IconListViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            if (item.selectable() && selectable) {
                lastSelected = new WeakReference<>(item);
                notifyDataSetChanged();
            }
            item.onClick();
        }

        @Override
        public boolean onLongClick(View v) {
            return item.onLongClick();
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
        if (viewType != R.layout.simple_list_divider) {
            view.setOnClickListener(vh);
            view.setOnLongClickListener(vh);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(IconListViewHolder holder, int position) {
        holder.item = items.get(position);
        holder.item.bindView(holder.itemView);
        if (lastSelected != null && lastSelected.get() == holder.item)
            holder.itemView.setSelected(true);
        else
            holder.itemView.setSelected(false);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public SimpleListFragmentAdapter(SimpleListFragment fragment) {
        this(fragment.getResources());
    }

    public SimpleListFragmentAdapter(Resources resources) {
        this.resources = resources;
    }

    public SimpleListItem getItem(int i) {
        return items.get(i);
    }

    protected void internalChanged() {
        if (sort != null)
            Collections.sort(items, sort);
        notifyDataSetChanged();
    }

    public SimpleListFragmentAdapter add(SimpleListItem item) {
        return insert(item, getItemCount());
    }

    public SimpleListFragmentAdapter remove(SimpleListItem item) {
        items.remove(item);
        internalChanged();
        return this;
    }

    public SimpleListFragmentAdapter clear() {
        items.clear();
        internalChanged();
        return this;
    }

    public SimpleListFragmentAdapter insert(SimpleListItem item, int index) {
        item.setAdapter(this);
        items.add(index, item);
        internalChanged();
        return this;
    }

    public SimpleListFragmentAdapter selectable(boolean selectable) {
        this.selectable = selectable;
        if (!selectable && lastSelected != null) {
            lastSelected = null;
            notifyDataSetChanged();
        }

        return this;
    }
}
