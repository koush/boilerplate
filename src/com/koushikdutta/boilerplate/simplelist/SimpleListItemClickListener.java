package com.koushikdutta.boilerplate.simplelist;

/**
 * Created by koush on 5/9/15.
 */
public interface SimpleListItemClickListener<T extends SimpleListItem> {
    void onClick(T item);
}
