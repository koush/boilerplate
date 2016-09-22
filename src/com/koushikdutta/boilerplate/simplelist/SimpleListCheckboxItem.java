package com.koushikdutta.boilerplate.simplelist;

import android.content.res.Resources;
import android.view.View;
import android.widget.CompoundButton;

import com.koushikdutta.boilerplate.R;

/**
 * Created by koush on 5/9/15.
 */
public class SimpleListCheckboxItem extends SimpleListOneLineItem {
    boolean checked;
    boolean showCheck = true;
    public SimpleListCheckboxItem(SimpleListFragmentAdapter adapter) {
        super(adapter);
    }

    public SimpleListCheckboxItem(SimpleListFragment fragment) {
        this(fragment.getAdapter());
    }

    public SimpleListCheckboxItem(Resources resources) {
        super(resources);
    }

    public boolean checked() {
        return checked;
    }

    public SimpleListCheckboxItem checked(boolean checked) {
        this.checked = checked;
        notifyDataSetChanged();
        return this;
    }

    public SimpleListCheckboxItem showCheck(boolean showCheck) {
        this.showCheck = showCheck;
        notifyDataSetChanged();
        return this;
    }

    public boolean showCheck() {
        return showCheck;
    }

    @Override
    protected void onClick() {
        checked(!checked());
        super.onClick();
    }

    @Override
    protected void bindView(View v) {
        super.bindView(v);
        CompoundButton c = (CompoundButton)v.findViewById(R.id.checkbox);
        c.setOnCheckedChangeListener(null);
        c.setChecked(checked);
        c.setVisibility(showCheck ? View.VISIBLE : View.GONE);
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
