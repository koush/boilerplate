package com.koushikdutta.boilerplate;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

/**
 * Created by koush on 3/29/15.
 */
public class IconListItem {
    Drawable icon;
    CharSequence title;
    IconListFragmentAdapter adapter;
    boolean selectableIcon;

    public IconListItem(IconListFragmentAdapter adapter) {
        this.adapter = adapter;
    }

    public IconListItem(IconListFragment fragment) {
        this(fragment.getAdapter());
    }

    public IconListItem title(CharSequence title) {
        this.title = title;
        adapter.notifyDataSetChanged();
        return this;
    }

    public IconListItem title(int title) {
        return title(adapter.getContext().getText(title));
    }

    public IconListItem icon(Drawable icon) {
        this.icon = icon;
        adapter.notifyDataSetChanged();
        return this;
    }

    public IconListItem icon(int icon) {
        return icon(adapter.getContext().getResources().getDrawable(icon));
    }

    public IconListItem selectableIcon(int icon) {
        selectableIcon = true;
        return icon(icon);
    }

    public IconListItem selectableIcon(Drawable drawable) {
        selectableIcon = true;
        return icon(drawable);
    }

    public void onClick(View v) {
    }

    public boolean onLongClick(View v) {
        return false;
    }

    protected void bindView(View v) {
        TintedImageView tiv = ((TintedImageView)v.findViewById(R.id.icon_list_fragment_icon));
        tiv.setImageDrawable(icon);
        tiv.setStateListFilter(TintedImageView.StateListFilter.Normal);
        ((TextView)v.findViewById(R.id.icon_list_fragment_title)).setText(title);
    }
}
