package com.koushikdutta.boilerplate.tint;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class TintedProgressBar extends ProgressBar {
    public TintedProgressBar(Context context) {
        super(context);
        init(null);
    }

    public TintedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TintedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        TintHelper.getColorPrimary(getContext());
        int tintColor = TintHelper.getColorAccent(getContext());
        if (attrs != null) {
            int color = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "tint", 0);
            if (color != 0)
                tintColor = getContext().getResources().getColor(color);
        }
        getIndeterminateDrawable().setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
        getProgressDrawable().setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
    }
}