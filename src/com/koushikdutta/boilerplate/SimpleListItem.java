package com.koushikdutta.boilerplate;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

/**
 * Created by koush on 3/29/15.
 */
public class SimpleListItem {
    private CharSequence title;
    protected SimpleListFragmentAdapter adapter;
    private View.OnClickListener onClick;

    protected Resources getResources() {
        return adapter.fragment.getResources();
    }

    public SimpleListItem(SimpleListFragmentAdapter adapter) {
        this.adapter = adapter;
    }

    public SimpleListItem(SimpleListFragment fragment) {
        this(fragment.getAdapter());
    }

    public SimpleListItem title(CharSequence title) {
        this.title = title;
        adapter.notifyDataSetChanged();
        return this;
    }

    public SimpleListItem title(int title) {
        return title(getResources().getText(title));
    }

    protected void onClick(View v) {
    }

    public boolean onLongClick(View v) {
        return false;
    }

    protected void bindView(View v) {
        ((TextView)v.findViewById(R.id.icon_list_fragment_title)).setText(title);
    }

    int getViewType() {
        return R.layout.icon_list_fragment_one_line_item;
    }
}
