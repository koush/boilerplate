package com.koushikdutta.boilerplate;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by koush on 3/30/15.
 */
public class TintedImageView extends ImageView {
    ColorStateList textColor;
    boolean stateListFilter;

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
        stateListFilter = attrs != null && attrs.getAttributeBooleanValue("http://schemas.android.com/apk/res-auto", "stateListFilter", false);
        textColor = TintHelper.getTintColorStateList(getContext());
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        update();
    }

    //To generate negative image
    private static final  float[] colorMatrix_Negative = {
    -1.0f, 0, 0, 0, 255, //red
    0, -1.0f, 0, 0, 255, //green
    0, 0, -1.0f, 0, 255, //blue
    0, 0, 0, 1.0f, 0 //alpha
    };

    private static final ColorMatrix colorMatrixNegative = new ColorMatrix(colorMatrix_Negative);

    void update() {
        if (stateListFilter) {
            int color = textColor.getColorForState(getDrawableState(), Color.TRANSPARENT);

            ColorMatrix s = new ColorMatrix();
            s.setScale(1 - Color.red(color) / 255f, 1 - Color.green(color) / 255f, 1 - Color.blue(color) / 255f, 1);
            s.preConcat(colorMatrixNegative);
            s.postConcat(colorMatrixNegative);

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(s);
            setColorFilter(filter);
        }
        else {
            setColorFilter(null);
        }
        invalidate();
    }

    public void setStateListFilter(boolean stateListFilter) {
        if (this.stateListFilter == stateListFilter)
            return;
        this.stateListFilter = stateListFilter;
        update();
    }
}
