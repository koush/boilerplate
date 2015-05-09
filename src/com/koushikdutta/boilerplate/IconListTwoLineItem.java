package com.koushikdutta.boilerplate;

/**
 * Created by koush on 5/9/15.
 */
public class IconListTwoLineItem extends IconListItem {
    CharSequence subtitle;

    public IconListTwoLineItem(IconListFragmentAdapter adapter) {
        super(adapter);
    }

    public IconListTwoLineItem(IconListFragment fragment) {
        this(fragment.getAdapter());
    }

    public IconListItem subtitle(CharSequence subtitle) {
        this.subtitle = subtitle;
        adapter.notifyDataSetChanged();
        return this;
    }
}
