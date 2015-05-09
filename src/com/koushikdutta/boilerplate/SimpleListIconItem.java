package com.koushikdutta.boilerplate;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by koush on 3/29/15.
 */
public class SimpleListIconItem extends SimpleListItem {
    private Drawable icon;
    private boolean selectableIcon;

    public SimpleListIconItem(SimpleListFragmentAdapter adapter) {
        super(adapter);
    }

    public SimpleListIconItem(SimpleListFragment fragment) {
        this(fragment.getAdapter());
    }

    public SimpleListIconItem icon(Drawable icon) {
        this.icon = icon;
        adapter.notifyDataSetChanged();
        return this;
    }

    public SimpleListIconItem icon(int icon) {
        return icon(getResources().getDrawable(icon));
    }

    public SimpleListIconItem selectableIcon(int icon) {
        selectableIcon = true;
        return icon(icon);
    }

    public SimpleListIconItem selectableIcon(Drawable drawable) {
        selectableIcon = true;
        return icon(drawable);
    }

    public void onClick(View v) {
    }

    public boolean onLongClick(View v) {
        return false;
    }

    protected void bindView(View v) {
        super.bindView(v);
        TintedImageView tiv = ((TintedImageView)v.findViewById(R.id.icon_list_fragment_icon));
        tiv.setImageDrawable(icon);
        if (selectableIcon)
            tiv.setStateListFilter(TintedImageView.StateListFilter.Normal);
        else
            tiv.setStateListFilter(TintedImageView.StateListFilter.None);
    }

    int getViewType() {
        return R.layout.icon_list_fragment_icon_one_line_item;
    }
}
