package com.koushikdutta.boilerplate;

/**
 * Created by koush on 5/9/15.
 */
public class IconListTwoLineCheckableItem extends IconListTwoLineItem {
    public IconListTwoLineCheckableItem(IconListFragmentAdapter adapter) {
        super(adapter);
    }

    public IconListTwoLineCheckableItem(IconListFragment fragment) {
        this(fragment.getAdapter());
    }

    public IconListItem subtitle(CharSequence subtitle) {
        this.subtitle = subtitle;
        adapter.notifyDataSetChanged();
        return this;
    }
}
