package com.koushikdutta.boilerplate.simplelist;

import android.view.View;
import android.widget.TextView;

import com.koushikdutta.boilerplate.R;

/**
 * Created by koush on 3/29/15.
 */
public class SimpleListOneLineItem extends SimpleListItem {
    private CharSequence title;

    public SimpleListOneLineItem(SimpleListFragmentAdapter adapter) {
        super(adapter);
    }

    public SimpleListOneLineItem(SimpleListFragment fragment) {
        this(fragment.getAdapter());
    }

    public SimpleListOneLineItem title(CharSequence title) {
        this.title = title;
        adapter.notifyDataSetChanged();
        return this;
    }

    public SimpleListOneLineItem title(int title) {
        return title(getResources().getText(title));
    }

    @Override
    protected void bindView(View v) {
        ((TextView)v.findViewById(R.id.icon_list_fragment_title)).setText(title);
    }

    @Override
    int getViewType() {
        return R.layout.simple_list_fragment_one_line_item;
    }
}
