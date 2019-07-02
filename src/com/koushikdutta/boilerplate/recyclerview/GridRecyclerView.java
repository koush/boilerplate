package com.koushikdutta.boilerplate.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.AttributeSet;

import com.koushikdutta.boilerplate.R;

import java.util.Hashtable;

/**
 * Created by koush on 4/19/15.
 */
public class GridRecyclerView extends HeaderRecyclerView {
    Hashtable<Integer, Integer> typeToSpan;
    GridLayoutManager gridLayoutManager;
    GridLayoutManager.SpanSizeLookup spanSizeLookup;

    public GridRecyclerView(Context context) {
        super(context);
    }

    public GridRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    void init(Context context, AttributeSet attrs, int defStyleAttr) {
        super.init(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(
        attrs, R.styleable.GridRecyclerView, defStyleAttr, 0);
        int numColumns = a.getInt(R.styleable.GridRecyclerView_numColumns, 1);
        setNumColumns(context, numColumns);
    }

    public void setNumColumns(int numColumns) {
        setNumColumns(getContext(), numColumns);
    }

    private void setNumColumns(Context context, int numColumns) {
        if (gridLayoutManager == null) {
            gridLayoutManager = new GridLayoutManager(context, numColumns);
            typeToSpan = new Hashtable<Integer, Integer>();
            gridLayoutManager.setSpanSizeLookup(spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getAdapter().getItemViewType(position);
                    Integer span = typeToSpan.get(viewType);
                    if (span != null)
                        return span;
                    ViewHolder vh = getAdapter().createViewHolder(GridRecyclerView.this, viewType);
                    int foundSpan;
                    if (vh instanceof SpanningViewHolder)
                        foundSpan = ((SpanningViewHolder)vh).getSpanSize(gridLayoutManager.getSpanCount());
                    else
                        foundSpan = 1;
                    typeToSpan.put(viewType, foundSpan);
                    return foundSpan;
                }
            });
            setLayoutManager(gridLayoutManager);
        }
        else {
            gridLayoutManager.setSpanCount(numColumns);
        }
        typeToSpan.clear();
        spanSizeLookup.invalidateSpanIndexCache();
        requestLayout();
    }

    public interface SpanningViewHolder {
        int getSpanSize(int spanCount);
    }
}
