package com.koushikdutta.boilerplate.simplelist;

import android.view.View;

import com.koushikdutta.boilerplate.R;

/**
 * Created by koush on 5/9/15.
 */
public class SimpleListDivider extends SimpleListItem {
    public SimpleListDivider(SimpleListFragmentAdapter adapter) {
        super(adapter);
    }

    public SimpleListDivider(SimpleListFragment fragment) {
        this(fragment.adapter);
    }

    @Override
    protected void bindView(View v) {
    }

    @Override
    int getViewType() {
        return R.layout.simple_list_divider;
    }
}
