package com.koushikdutta.boilerplate.simplelist;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.koushikdutta.boilerplate.R;

/**
 * Created by koush on 5/9/15.
 */
public class SimpleListTwoLineItem extends SimpleListOneLineItem {
    CharSequence subtitle;

    public SimpleListTwoLineItem(SimpleListFragmentAdapter adapter) {
        super(adapter);
    }

    public SimpleListTwoLineItem(SimpleListFragment fragment) {
        this(fragment.getAdapter());
    }

    public SimpleListTwoLineItem(Resources resources) {
        super(resources);
    }

    public SimpleListTwoLineItem subtitle(CharSequence subtitle) {
        this.subtitle = subtitle;
        notifyDataSetChanged();
        return this;
    }

    public SimpleListTwoLineItem subtitle(int subtitle) {
        subtitle(getResources().getString(subtitle));
        return this;
    }

    @Override
    protected void bindView(View v) {
        super.bindView(v);
        TextView tv = (TextView)v.findViewById(R.id.icon_list_fragment_subtitle);
        tv.setText(subtitle);
        tv.setVisibility(subtitle != null ? TextView.VISIBLE : TextView.GONE);
    }

    @Override
    int getViewType() {
        return R.layout.simple_list_fragment_two_line_item;
    }
}
