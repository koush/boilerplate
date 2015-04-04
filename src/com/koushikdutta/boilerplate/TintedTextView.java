package com.koushikdutta.boilerplate;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by koush on 3/30/15.
 */
public class TintedTextView extends TextView {
    public TintedTextView(Context context) {
        super(context);
        init();
    }

    public TintedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TintedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTextColor(TintHelper.getTintColorStateList(getContext()));
    }
}
