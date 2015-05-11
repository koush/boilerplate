package com.koushikdutta.boilerplate.tint;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.koushikdutta.boilerplate.R;

/**
 * Created by koush on 3/30/15.
 */
public class TintHelper {
    public static int getTextColorPrimary(Context context) {
        return getStyledColor(context, android.R.attr.textColorPrimary);
    }

    public static int getTextColorPrimaryInverse(Context context) {
        return getStyledColor(context, android.R.attr.textColorPrimaryInverse);
    }

    public static int getColorPrimaryDark(Context context) {
        return getStyledColor(context, R.attr.colorPrimaryDark);
    }

    public static int getColorPrimary(Context context) {
        return getStyledColor(context, R.attr.colorPrimary);
    }

    public static int getColorAccent(Context context) {
        return getStyledColor(context, R.attr.colorAccent);
    }

    public static int getStyledColor(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

    public static ColorStateList getTintColorStateList(Context context) {
        return getTintColorStateList(context, getTextColorPrimary(context));
    }

    public static ColorStateList getInverseTintColorStateList(Context context) {
        return getTintColorStateList(context, getTextColorPrimaryInverse(context));
    }

    public static ColorStateList getTintColorStateList(Context context, int textColorPrimary) {
        int colorPrimary = getColorPrimary(context);

        return new ColorStateList(new int[][]{
        new int[]{android.R.attr.state_pressed},
        new int[]{android.R.attr.state_focused},
        new int[]{android.R.attr.state_selected},
        new int[]{android.R.attr.state_checked},
        new int[]{android.R.attr.state_active},
        new int[]{android.R.attr.state_activated},
        new int[]{}
        },
        new int[] {
        colorPrimary,
        colorPrimary,
        colorPrimary,
        colorPrimary,
        colorPrimary,
        colorPrimary,
        textColorPrimary
        });
    }

    //To generate negative image
    private static final  float[] colorMatrix_Negative = {
    -1.0f, 0, 0, 0, 255, //red
    0, -1.0f, 0, 0, 255, //green
    0, 0, -1.0f, 0, 255, //blue
    0, 0, 0, 1.0f, 0 //alpha
    };
    private static final ColorMatrix colorMatrixNegative = new ColorMatrix(colorMatrix_Negative);

    static void setColorFilter(Drawable drawable, int color) {
        ColorMatrix s = new ColorMatrix();
        s.setScale(1 - Color.red(color) / 255f, 1 - Color.green(color) / 255f, 1 - Color.blue(color) / 255f, 1);
        s.preConcat(colorMatrixNegative);
        s.postConcat(colorMatrixNegative);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(s);
        drawable.setColorFilter(filter);
    }

    public static Drawable getStateListDrawable(Context context, Drawable drawable, int textColorPrimary) {
        if (drawable == null)
            return null;
        int colorPrimary = getColorPrimary(context);

        return getStateListDrawable(drawable, colorPrimary, textColorPrimary);
    }

    public static Drawable getColorMatrixStateListDrawable(Context context, Drawable drawable, int textColorPrimary) {
        if (drawable == null)
            return null;
        int colorPrimary = getColorPrimary(context);

        SelectorDrawable ret = getStateListDrawable(drawable, colorPrimary, textColorPrimary);
        ret.porterDuff = true;
        return ret;
    }

    private static SelectorDrawable getStateListDrawable(Drawable drawable, int colored, int normal) {
        drawable = drawable.getConstantState().newDrawable().mutate();
        SelectorDrawable ret = new SelectorDrawable();
        ret.colored = colored;
        ret.normal = normal;
        ret.addState(new int[]{android.R.attr.state_pressed}, drawable);
        ret.addState(new int[]{android.R.attr.state_focused}, drawable);
        ret.addState(new int[]{android.R.attr.state_selected}, drawable);
        ret.addState(new int[]{android.R.attr.state_checked}, drawable);
        ret.addState(new int[]{android.R.attr.state_active}, drawable);
        ret.addState(new int[]{android.R.attr.state_activated}, drawable);
        ret.addState(new int[]{}, drawable);

        return ret;
    }
}
