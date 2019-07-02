package com.koushikdutta.boilerplate;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;

/**
 * Created by koush on 4/8/15.
 */
public final class WindowChromeUtils {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static ValueAnimator statusBarFadeToColorLollipop(Context context, ValueAnimator currentAnimation, int color) {
        if (currentAnimation != null) {
            currentAnimation.end();
            if ((int)currentAnimation.getAnimatedValue() == color)
                return currentAnimation;
            currentAnimation.cancel();
        }
        if (!(context instanceof Activity))
            return null;
        final Window window = ((Activity)context).getWindow();
        currentAnimation = ValueAnimator.ofArgb(window.getStatusBarColor(), color);
        currentAnimation.setDuration(context.getResources().getInteger(android.R.integer.config_longAnimTime));
        currentAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentColor = (int) animation.getAnimatedValue();
                window.setStatusBarColor(currentColor);
            }
        });
        currentAnimation.start();
        return currentAnimation;
    }

    public static ValueAnimator statusBarFadeToColor(Context context, ValueAnimator existingStatusBarAnimation, int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return null;
        return statusBarFadeToColorLollipop(context, existingStatusBarAnimation, color);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static ValueAnimator navigationBarFadeToColorLollipop(Context context, ValueAnimator currentAnimation, int color) {
        if (currentAnimation != null) {
            currentAnimation.end();
            if ((int)currentAnimation.getAnimatedValue() == color)
                return currentAnimation;
            currentAnimation.cancel();
        }
        final Window window = ((Activity)context).getWindow();
        currentAnimation = ValueAnimator.ofArgb(window.getNavigationBarColor(), color);
        currentAnimation.setDuration(context.getResources().getInteger(android.R.integer.config_longAnimTime));
        currentAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentColor = (int) animation.getAnimatedValue();
                window.setNavigationBarColor(currentColor);
            }
        });
        currentAnimation.start();
        return currentAnimation;
    }

    public static ValueAnimator navigationBarFadeToColor(Context context, ValueAnimator existingStatusBarAnimation, int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return null;
        return navigationBarFadeToColorLollipop(context, existingStatusBarAnimation, color);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static int getStatusBarColorLollipop(Window window) {
        return window.getStatusBarColor();
    }

    public static int getStatusBarColor(Window window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return 0;
        return getStatusBarColorLollipop(window);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setStatusBarColorLollipop(Window window, int color) {
        window.setStatusBarColor(color);
    }

    public static void setStatusBarColor(Window window, int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return;
        setStatusBarColorLollipop(window, color);
    }
}
