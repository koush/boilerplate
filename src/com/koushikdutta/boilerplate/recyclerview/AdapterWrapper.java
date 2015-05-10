package com.koushikdutta.boilerplate.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koushikdutta.boilerplate.R;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by koush on 4/19/15.
 */
public class AdapterWrapper extends RecyclerView.Adapter {
    // start at 1, 0 is header.
    int viewTypeMapCount = 1;
    Hashtable<Integer, WrappedAdapter> viewTypes = new Hashtable<Integer, WrappedAdapter>();

    private int nextEmptyViewType() {
        // empty views are always even view types
        if (viewTypeMapCount % 2 == 1)
            viewTypeMapCount++;
        return viewTypeMapCount++;
    }

    private int nextViewType() {
        // views are always odd view types
        if (viewTypeMapCount % 2 == 0)
            viewTypeMapCount++;
        return viewTypeMapCount++;
    }

    private int getAdapterStartPosition(RecyclerView.Adapter adapter) {
        int count = 0;
        for (WrappedAdapter info: adapters) {
            if (info.adapter == adapter)
                return count;
            int adapterCount = info.adapter.getItemCount();
            count += adapterCount;
            // header check
            if (info.isShowingHeader())
                count++;
            if (info.isShowingEmptyView())
                count++;
        }
        throw new RuntimeException("invalid adapter");
    }

    public class WrappedAdapter extends RecyclerView.AdapterDataObserver {
        String sectionHeader;
        View emptyView;
        int emptyViewType;

        private boolean isShowingHeader() {
            return sectionHeader != null && adapter.getItemCount() > 0;
        }

        private boolean isShowingEmptyView() {
            return emptyView != null && adapter.getItemCount() == 0;
        }

        public WrappedAdapter sectionHeader(String sectionHeader) {
            this.sectionHeader = sectionHeader;
            notifyDataSetChanged();
            return this;
        }

        public WrappedAdapter sectionHeader(Context context, int string) {
            return sectionHeader(context.getString(string));
        }

        public WrappedAdapter emptyView(View emptyView) {
            this.emptyView = emptyView;
            emptyViewType = nextEmptyViewType();
            notifyDataSetChanged();
            return this;
        }

        public WrappedAdapter emptyView(Context context, int emptyView) {
            return emptyView(LayoutInflater.from(context).inflate(emptyView, null));
        }

        Hashtable<Integer, Integer> viewTypes = new Hashtable<Integer, Integer>();
        Hashtable<Integer, Integer> unmappedViewTypes = new Hashtable<Integer, Integer>();
        RecyclerView.Adapter adapter;

        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            int startPosition = getAdapterStartPosition(adapter);
            notifyItemRangeChanged(startPosition + positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            int startPosition = getAdapterStartPosition(adapter);
            notifyItemRangeInserted(startPosition + positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            int startPosition = getAdapterStartPosition(adapter);
            notifyItemRangeRemoved(startPosition + positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            int startPosition = getAdapterStartPosition(adapter);
            for (int i = 0; i < itemCount; i++) {
                notifyItemMoved(fromPosition + startPosition + i, toPosition + startPosition + i);
            }
        }
    }

    ArrayList<WrappedAdapter> adapters = new ArrayList<WrappedAdapter>();

    public WrappedAdapter wrapAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null)
            throw new AssertionError("adapter must not be null");
        WrappedAdapter info = new WrappedAdapter();
        info.adapter = adapter;
        adapters.add(info);
        adapter.registerAdapterDataObserver(info);
        notifyDataSetChanged();
        return info;
    }

    public void remove(RecyclerView.Adapter adapter) {
        for(Iterator<Map.Entry<Integer, WrappedAdapter>> it = viewTypes.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Integer, WrappedAdapter> entry = it.next();
            if(entry.getValue().adapter == adapter)
                it.remove();
        }

        for (int i = 0; i < adapters.size(); i++) {
            if (adapters.get(i).adapter == adapter) {
                unregisterAdapterDataObserver(adapters.remove(i));
                notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        for (WrappedAdapter info: adapters) {
            // header check
            if (info.isShowingHeader()) {
                if (position == 0)
                    return 0;
                position--;
            }
            if (position < info.adapter.getItemCount()) {
                final int viewType;
                boolean isEmpty = info.isShowingEmptyView();
                if (isEmpty)
                    viewType = info.emptyViewType;
                else
                    viewType = info.adapter.getItemViewType(position);
                if (!info.viewTypes.containsKey(viewType)) {
                    int mappedViewType = isEmpty ? nextEmptyViewType() : nextViewType();
                    viewTypes.put(mappedViewType, info);
                    info.viewTypes.put(viewType, mappedViewType);
                    info.unmappedViewTypes.put(mappedViewType, viewType);
                    return mappedViewType;
                }
                else {
                    return info.viewTypes.get(viewType);
                }
            }
            position -= info.adapter.getItemCount();
        }
        throw new RuntimeException("invalid position");
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder implements GridRecyclerView.SpanningViewHolder {
        TextView textView;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(android.R.id.text1);
        }

        public void bind(WrappedAdapter info) {
            textView.setText(info.sectionHeader);
        }

        @Override
        public int getSpanSize(int spanCount) {
            return spanCount;
        }
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder implements GridRecyclerView.SpanningViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public int getSpanSize(int spanCount) {
            return spanCount;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View header = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_wrapper_list_header, parent, false);
            return new HeaderViewHolder(header);
        }
        WrappedAdapter info = viewTypes.get(viewType);
        int unmappedViewType = info.unmappedViewTypes.get(viewType);
        if (info.emptyViewType == viewType)
            return new EmptyViewHolder(info.emptyView);
        return info.adapter.onCreateViewHolder(parent, unmappedViewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EmptyViewHolder) {
            // no op, nothing to bind.
            return;
        }
        for (WrappedAdapter info: adapters) {
            // header check
            if (info.isShowingHeader()) {
                if (position == 0) {
                    ((HeaderViewHolder)holder).bind(info);
                    return;
                }
                position--;
            }
            if (position < info.adapter.getItemCount()) {
                info.adapter.onBindViewHolder(holder, position);
                return;
            }
            position -= info.adapter.getItemCount();
        }
        throw new RuntimeException("invalid position");
    }

    private int getItemCount(boolean withEmptyView) {
        int count = 0;
        for (WrappedAdapter info: adapters) {
            int adapterCount = info.adapter.getItemCount();
            count += adapterCount;
            // header check
            if (info.isShowingHeader())
                count++;
            if (info.isShowingEmptyView())
                count++;
        }
        return count;
    }

    public boolean isEmptyView(int position) {
        return getItemViewType(position) % 2 == 0;
    }

    public boolean isHeaderView(int position) {
        return getItemViewType(position) == 0;
    }

    @Override
    public int getItemCount() {
        return getItemCount(true);
    }
}
