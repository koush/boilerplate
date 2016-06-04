package com.koushikdutta.boilerplate.simplelist;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koushikdutta.boilerplate.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by koush on 3/29/15.
 */
public class SimpleListFragmentAdapter extends RecyclerView.Adapter<SimpleListFragmentAdapter.IconListViewHolder> {
    ArrayList<SimpleListItem> items = new ArrayList<SimpleListItem>();
    Resources resources;
    boolean selectable = true;
    WeakReference<View> lastSelected;

    public class IconListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        SimpleListItem item;
        public IconListViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            if (item.selectable() && selectable) {
                if (lastSelected != null) {
                    View l = lastSelected.get();
                    if (l != null)
                        l.setSelected(false);
                }
                v.setSelected(true);
                lastSelected = new WeakReference<View>(v);
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
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public SimpleListFragmentAdapter(SimpleListFragment fragment) {
        this.resources = fragment.getResources();
    }

    public SimpleListFragmentAdapter(Resources resources) {
        this.resources = resources;
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
            View l = lastSelected.get();
            if (l != null)
                l.setSelected(false);
            lastSelected = null;
        }
    }
}
