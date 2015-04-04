package com.koushikdutta.boilerplate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by koush on 3/29/15.
 */
public abstract class IconListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    IconListFragmentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new IconListFragmentAdapter(getActivity());
    }

    public static ListView getIconListFragmentListView(View view) {
        return (ListView)view.findViewById(android.R.id.list);
    }

    public static ViewGroup getIconListViewContainer(View view) {
        return (ViewGroup)view.findViewById(R.id.list_container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lv = getIconListFragmentListView(view);
        lv.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.icon_list_fragment, null);

        ListView lv = getIconListFragmentListView(ret);
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
        return ret;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView lv = (ListView) parent;
        if (position < lv.getHeaderViewsCount())
            return;
        adapter.getItem(position - lv.getHeaderViewsCount()).onClick(view);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ListView lv = (ListView) parent;
        if (position < lv.getHeaderViewsCount())
            return false;
        return adapter.getItem(position - lv.getHeaderViewsCount()).onLongClick(view);
    }

    public IconListFragmentAdapter getAdapter() {
        return adapter;
    }

    public IconListFragment removeItem(IconListItem li) {
        getAdapter().remove(li);
        return this;
    }

    public IconListFragment addItem(IconListItem li) {
        getAdapter().add(li);
        return this;
    }

    public IconListFragment insertItem(IconListItem li) {
        getAdapter().insert(li, 0);
        return this;
    }

    public IconListFragment clearItems() {
        adapter.clear();
        return this;
    }
}
