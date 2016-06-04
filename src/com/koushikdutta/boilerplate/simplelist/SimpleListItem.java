package com.koushikdutta.boilerplate.simplelist;

import android.content.res.Resources;
import android.view.View;

/**
 * Created by koush on 3/29/15.
 */
public abstract class SimpleListItem {
    private boolean selectable = true;
    protected SimpleListFragmentAdapter adapter;
    private SimpleListItemClickListener onClick;
    private SimpleListItemLongClickListener onLongClick;

    protected Resources getResources() {
        return adapter.resources;
    }

    public SimpleListItem(SimpleListFragmentAdapter adapter) {
        this.adapter = adapter;
    }

    public SimpleListItem(SimpleListFragment fragment) {
        this(fragment.getAdapter());
    }

    void onClick() {
        if (onClick != null)
            onClick.onClick(this);
    }

    boolean onLongClick() {
        if (onLongClick != null)
            return onLongClick.onLongClick(this);
        return false;
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
