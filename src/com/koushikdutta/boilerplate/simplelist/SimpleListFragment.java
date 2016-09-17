package com.koushikdutta.boilerplate.simplelist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.koushikdutta.boilerplate.R;
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

    public static GridRecyclerView getGridRecyclerView(View view) {
        return (GridRecyclerView)view.findViewById(android.R.id.list);
    }

    public GridRecyclerView getGridRecyclerView() {
        View view = getView();
        if (view == null)
            return null;
        return getGridRecyclerView(view);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridRecyclerView lv = getGridRecyclerView(view);
        lv.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simple_list_fragment, null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView lv = (ListView) parent;
        if (position < lv.getHeaderViewsCount())
            return;
        adapter.getItem(position - lv.getHeaderViewsCount()).onClick();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ListView lv = (ListView) parent;
        if (position < lv.getHeaderViewsCount())
            return false;
        return adapter.getItem(position - lv.getHeaderViewsCount()).onLongClick();
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

    public SimpleListFragment insertItem(int index, SimpleListItem li) {
        getAdapter().insert(li, index);
        return this;
    }

    public SimpleListFragment clearItems() {
        adapter.clear();
        return this;
    }

    public int getItemCount() {
        return adapter.getItemCount();
    }

    public void setSelectable(boolean selectable) {
        adapter.selectable(selectable);
    }
}
