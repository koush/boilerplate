package com.koushikdutta.boilerplate.simplelist;

/**
 * Created by koush on 5/9/15.
 */
public interface SimpleListItemLongClickListener<T extends SimpleListItem> {
    boolean onLongClick(T item);
}
