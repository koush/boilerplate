package com.koushikdutta.boilerplate.simplelist;

import android.view.View;
import android.widget.TextView;

import com.koushikdutta.boilerplate.R;

/**
 * Created by koush on 5/9/15.
 */
public class SimpleListTwoLineCheckboxItem extends SimpleListCheckboxItem {
    CharSequence subtitle;

    public SimpleListTwoLineCheckboxItem(SimpleListFragmentAdapter adapter) {
        super(adapter);
    }

    public SimpleListTwoLineCheckboxItem(SimpleListFragment fragment) {
        this(fragment.getAdapter());
    }

    public SimpleListTwoLineCheckboxItem subtitle(CharSequence subtitle) {
        this.subtitle = subtitle;
        adapter.notifyDataSetChanged();
        return this;
    }

    public SimpleListTwoLineCheckboxItem subtitle(int subtitle) {
        subtitle(getResources().getString(subtitle));
        return this;
    }

    @Override
    protected void bindView(View v) {
        super.bindView(v);
        ((TextView)v.findViewById(R.id.icon_list_fragment_subtitle)).setText(subtitle);
    }

    @Override
    int getViewType() {
        return R.layout.simple_list_fragment_two_line_checkbox_item;
    }
}
