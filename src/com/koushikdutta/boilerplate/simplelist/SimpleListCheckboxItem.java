package com.koushikdutta.boilerplate.simplelist;

import android.view.View;
import android.widget.CompoundButton;

import com.koushikdutta.boilerplate.R;

/**
 * Created by koush on 5/9/15.
 */
public class SimpleListCheckboxItem extends SimpleListOneLineItem {
    boolean checked;
    public SimpleListCheckboxItem(SimpleListFragmentAdapter adapter) {
        super(adapter);
    }

    public SimpleListCheckboxItem(SimpleListFragment fragment) {
        this(fragment.getAdapter());
    }

    public boolean checked() {
        return checked;
    }

    public SimpleListCheckboxItem checked(boolean checked) {
        this.checked = checked;
        adapter.notifyDataSetChanged();
        return this;
    }

    @Override
    void onClick() {
        checked(!checked());
        super.onClick();
    }

    @Override
    protected void bindView(View v) {
        super.bindView(v);
        CompoundButton c = (CompoundButton)v.findViewById(R.id.checkbox);
        c.setOnCheckedChangeListener(null);
        c.setChecked(checked);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;
                SimpleListCheckboxItem.super.onClick();
            }
        });
    }

    @Override
    int getViewType() {
        return R.layout.simple_list_fragment_one_line_checkbox_item;
    }
}
