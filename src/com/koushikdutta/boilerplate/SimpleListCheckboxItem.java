package com.koushikdutta.boilerplate;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by koush on 5/9/15.
 */
public class SimpleListCheckboxItem extends SimpleListItem {
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
    protected void onClick(View v) {
        super.onClick(v);
        checked(!checked());
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
                SimpleListCheckboxItem.super.onClick(buttonView);
            }
        });
    }

    @Override
    int getViewType() {
        return R.layout.icon_list_fragment_one_line_checkbox_item;
    }
}
