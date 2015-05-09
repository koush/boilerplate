package com.koushikdutta.boilerplate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.koushikdutta.boilerplate.recyclerview.GridRecyclerView;

/**
 * Created by koush on 3/29/15.
 */
public class SimpleListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    SimpleListFragmentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new SimpleListFragmentAdapter(this);
    }

    public static GridRecyclerView getIconListFragmentListView(View view) {
        return (GridRecyclerView)view.findViewById(android.R.id.list);
    }

    public static ViewGroup getIconListViewContainer(View view) {
        return (ViewGroup)view.findViewById(R.id.list_container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridRecyclerView lv = getIconListFragmentListView(view);
        lv.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.icon_list_fragment, null);
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

    public SimpleListFragmentAdapter getAdapter() {
        return adapter;
    }

    public SimpleListFragment removeItem(SimpleListItem li) {
        getAdapter().remove(li);
        return this;
    }

    public SimpleListFragment addItem(SimpleListItem li) {
        getAdapter().add(li);
        return this;
    }

    public SimpleListFragment insertItem(SimpleListItem li) {
        getAdapter().insert(li, 0);
        return this;
    }

    public SimpleListFragment clearItems() {
        adapter.clear();
        return this;
    }

    public void setSelectable(boolean selectable) {
        adapter.setSelectable(selectable);
    }
}
