package com.koushikdutta.boilerplate.tint;

import android.graphics.PorterDuff;
import android.graphics.drawable.StateListDrawable;

class SelectorDrawable extends StateListDrawable {
    int colored;
    int normal;
    boolean porterDuff;

    @Override
    protected boolean onStateChange(int[] states) {
        boolean isColored = false;
        for (int state : states) {
            switch (state) {
                case android.R.attr.state_pressed:
                case android.R.attr.state_focused:
                case android.R.attr.state_selected:
                case android.R.attr.state_checked:
                case android.R.attr.state_active:
                case android.R.attr.state_activated:
                    isColored = true;
                    break;
            }
        }

        if (porterDuff) {
            if (!isColored)
                setColorFilter(normal, PorterDuff.Mode.SRC_IN);
            else
                setColorFilter(colored, PorterDuff.Mode.SRC_IN);
        }
        else {
            if (!isColored)
                TintHelper.setColorFilter(this, normal);
            else
                TintHelper.setColorFilter(this, colored);
        }
        return super.onStateChange(states);
    }
}