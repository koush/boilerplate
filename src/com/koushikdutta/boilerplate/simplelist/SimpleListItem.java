package com.koushikdutta.boilerplate.simplelist;

import android.content.res.Resources;
import android.view.View;

/**
 * Created by koush on 3/29/15.
 */
public abstract class SimpleListItem {
    private boolean selectable = true;
    private Resources resources;
    private SimpleListFragmentAdapter adapter;
    private SimpleListItemClickListener onClick;
    private SimpleListItemLongClickListener onLongClick;

    protected void notifyDataSetChanged() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    protected Resources getResources() {
        return resources;
    }

    public SimpleListItem(Resources resources) {
        this.resources = resources;
    }

    public SimpleListItem(SimpleListFragmentAdapter adapter) {
        this(adapter.resources);
    }

    public SimpleListItem(SimpleListFragment fragment) {
        this(fragment.getAdapter());
    }

    void setAdapter(SimpleListFragmentAdapter adapter) {
        this.adapter = adapter;
    }

    public void invokeClick() {
        if (onClick != null)
            onClick.onClick(this);
    }

    public boolean invokeLongClick() {
        if (onLongClick != null)
            return onLongClick.onLongClick(this);
        return false;
    }

    protected void onClick() {
        invokeClick();
    }

    protected boolean onLongClick() {
        return invokeLongClick();
    }

    public <T extends SimpleListItem> T click(SimpleListItemClickListener<T> listener) {
        onClick = listener;
        return (T)this;
    }

    public <T extends SimpleListItem> T longClick(SimpleListItemLongClickListener<T> listener) {
        onLongClick = listener;
        return (T)this;
    }

    protected abstract void bindView(View v);

    abstract int getViewType();

    public boolean selectable() {
        return selectable;
    }

    public SimpleListItem selectable(boolean selectable) {
        this.selectable = selectable;
        return this;
    }
}
