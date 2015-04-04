package com.koushikdutta.boilerplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by koush on 3/29/15.
 */
public class IconListFragmentAdapter extends ArrayAdapter<IconListItem> {
    LayoutInflater inflater;

    public IconListFragmentAdapter(Context context) {
        super(context, R.layout.icon_list_fragment_item);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.icon_list_fragment_item, null);
        }

        IconListItem li = getItem(position);
        li.bindView(convertView);
        return convertView;
    }
}
