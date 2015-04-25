package com.koushikdutta.boilerplate;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by koush on 3/30/15.
 */
public class TintedImageView extends ImageView {
    public enum StateListFilter {
        None,
        Normal,
        Inverse
    }

    int stateListFilter;

    public TintedImageView(Context context) {
        super(context);
        init(null);
    }

    public TintedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TintedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        stateListFilter = attrs != null ? attrs.getAttributeIntValue("http://schemas.android.com/apk/res-auto", "stateListFilter", 0) : 0;
        setImageDrawable(original);
    }

    Drawable original;
    @Override
    public void setImageDrawable(Drawable drawable) {
        original = drawable;
        if (stateListFilter != 0) {
            if (stateListFilter == 1)
                drawable = TintHelper.getStateListDrawable(getContext(), drawable, TintHelper.getTextColorPrimary(getContext()));
            else
                drawable = TintHelper.getStateListDrawable(getContext(), drawable, TintHelper.getTextColorPrimaryInverse(getContext()));
        }
        super.setImageDrawable(drawable);
    }

    public void setStateListFilter(StateListFilter stateListFilter) {
        if (this.stateListFilter == stateListFilter.ordinal())
            return;
        this.stateListFilter = stateListFilter.ordinal();
        setImageDrawable(original);
    }
}
