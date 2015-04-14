package com.koushikdutta.boilerplate;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by koush on 3/30/15.
 */
public class TintHelper {
    public static int getTextColorPrimary(Context context) {
        return getStyledColor(context, android.R.attr.textColorPrimary);
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
        int textColorPrimary = getTextColorPrimary(context);
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
}
