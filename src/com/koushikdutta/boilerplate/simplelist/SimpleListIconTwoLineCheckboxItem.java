package com.koushikdutta.boilerplate.simplelist;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.koushikdutta.boilerplate.R;
import com.koushikdutta.boilerplate.tint.TintedImageView;

/**
 * Created by koush on 5/9/15.
 */
public class SimpleListIconTwoLineCheckboxItem extends SimpleListTwoLineCheckboxItem {
    private Drawable icon;
    private boolean selectableIcon;

    public SimpleListIconTwoLineCheckboxItem(SimpleListFragmentAdapter adapter) {
        super(adapter);
    }

    public SimpleListIconTwoLineCheckboxItem(SimpleListFragment fragment) {
        this(fragment.getAdapter());
    }

    public SimpleListIconTwoLineCheckboxItem(Resources resources) {
        super(resources);
    }

    public SimpleListIconTwoLineCheckboxItem icon(Drawable icon) {
        this.icon = icon;
        notifyDataSetChanged();
        return this;
    }

    public SimpleListIconTwoLineCheckboxItem icon(int icon) {
        return icon(getResources().getDrawable(icon));
    }

    public SimpleListIconTwoLineCheckboxItem tintableIcon(int icon) {
        selectableIcon = true;
        return icon(icon);
    }

    public SimpleListIconTwoLineCheckboxItem tintableIcon(Drawable drawable) {
        selectableIcon = true;
        return icon(drawable);
    }

    protected void bindImageView(TintedImageView tiv) {
        tiv.setImageDrawable(icon);
        if (selectableIcon)
            tiv.setStateListFilter(TintedImageView.StateListFilter.Normal);
        else
            tiv.setStateListFilter(TintedImageView.StateListFilter.None);
    }

    protected void bindView(View v) {
        super.bindView(v);
        TintedImageView tiv = ((TintedImageView)v.findViewById(R.id.icon_list_fragment_icon));
        bindImageView(tiv);
    }

    @Override
    int getViewType() {
        return R.layout.simple_list_fragment_icon_two_line_checkbox_item;
    }
}
