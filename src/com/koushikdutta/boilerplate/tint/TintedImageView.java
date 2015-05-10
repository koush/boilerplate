package com.koushikdutta.boilerplate.tint;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by koush on 3/30/15.
 */
public class TintedImageView extends ImageView {
    public enum StateListFilter {
        None,
        Normal,
        Inverse,
        ColorFilter
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
            else if (stateListFilter == 2)
                drawable = TintHelper.getStateListDrawable(getContext(), drawable, TintHelper.getTextColorPrimaryInverse(getContext()));
            else
                drawable = TintHelper.getColorMatrixStateListDrawable(getContext(), drawable, TintHelper.getTextColorPrimary(getContext()));
        }
        super.setImageDrawable(drawable);
    }

    @Override
    public void setImageResource(int resId) {
        setImageDrawable(getContext().getResources().getDrawable(resId));
    }

    public void setStateListFilter(StateListFilter stateListFilter) {
        if (this.stateListFilter == stateListFilter.ordinal())
            return;
        this.stateListFilter = stateListFilter.ordinal();
        setImageDrawable(original);
    }
}
