package com.koushikdutta.boilerplate;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * Created by koush on 4/4/15.
 */
public class ScrollingToolbarLayout extends FrameLayout {
    public ScrollingToolbarLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ScrollingToolbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ScrollingToolbarLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        colorPrimary = TintHelper.getColorPrimary(context);
        colorFaded = 0;
        colorPrimaryDark = TintHelper.getStyledColor(context, R.attr.colorPrimaryDark);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            currentStatusBarColor = colorPrimaryDark;
        else
            currentStatusBarColor = colorPrimary;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (scrollOffEnabled)
            return;

        if (getChildCount() == 0)
            throw new RuntimeException(getClass().getSimpleName() + " must contain at least one child View.");

        if (getChildCount() > 3)
            throw new RuntimeException(getClass().getSimpleName() + " ay only contain a maxmimum of 3 Views. Backdrop, Content, and Toolbar, in that order.");

        toolbar = getChildAt(getChildCount() - 1);

        int contentIndex = getChildCount() == 3 ? 1 : 0;
        View content = getChildAt(contentIndex);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height - toolbar.getMeasuredHeight(), heightMode);

        content.measure(widthMeasureSpec, newHeightMeasureSpec);
    }

    View toolbar;
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0)
            throw new RuntimeException(getClass().getSimpleName() + " must contain at least one child View.");

        if (getChildCount() > 3)
            throw new RuntimeException(getClass().getSimpleName() + " ay only contain a maxmimum of 3 Views. Backdrop, Content, and Toolbar, in that order.");

        toolbar = getChildAt(getChildCount() - 1);
        toolbar.layout(l, t, r, t + toolbar.getMeasuredHeight());

        int newt;
        if (scrollOffEnabled)
            newt = t;
        else
            newt = t + (toolbar != null ? toolbar.getMeasuredHeight() : 0);

        int curChild = 0;
        if (getChildCount() == 3) {
            View child = getChildAt(curChild++);
            child.layout(l, newt, r, child.getMeasuredHeight());
        }

        View content = getChildAt(curChild);
        content.layout(l, newt, r, b);
    }

    boolean scrollOffEnabled;
    public void enableToolbarScrollOff(final ListView listView, final AbsListView.OnScrollListener scrollListener) {
        enableToolbarScrollOff(new HeaderAbsListView() {
            @Override
            public void addHeaderView(View view) {
                listView.addHeaderView(view);
            }

            @Override
            public void setOnScrollListener(AbsListView.OnScrollListener l) {
                listView.setOnScrollListener(l);
            }
        }, scrollListener);
    }

    public void enableToolbarScrollOff(final AbsListView listView, AbsListView.OnScrollListener scrollListener) {
        if (listView instanceof ListView)
            enableToolbarScrollOff((ListView)listView, scrollListener);
        else
            enableToolbarScrollOff((HeaderAbsListView)listView, scrollListener);
    }

    public void enableToolbarScrollOff(HeaderAbsListView listView, final AbsListView.OnScrollListener scrollListener) {
        scrollOffEnabled = true;

        int extra;
        if (getChildCount() == 3) {
            extra = getChildAt(0).getLayoutParams().height;
        }
        else {
            extra = getChildAt(getChildCount() - 1).getLayoutParams().height;
        }

        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, extra);
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(lp);
        listView.addHeaderView(frameLayout);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollListener != null)
                    scrollListener.onScrollStateChanged(view, scrollState);
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (scrollListener != null)
                    scrollListener.onScroll(absListView, firstVisibleItem, visibleItemCount, totalItemCount);

                if (absListView.getChildCount() < 1) {
                    return;
                }

                final View firstView = absListView.getChildAt(0);

                final View toolbarContainer = getChildAt(getChildCount() - 1);
                final View backdrop;
                if (getChildCount() == 3)
                    backdrop = getChildAt(0);
                else
                    backdrop = null;
                final int toolbarHeight = toolbarContainer.getHeight();

                if (backdrop != null) {
                    int newBackdropHeight;
                    int backdropHeight = getResources().getDimensionPixelSize(R.dimen.icon_list_drawer_activity_backdrop_height);
                    if (firstVisibleItem >= 1) {
                        newBackdropHeight = toolbarHeight;
                    } else {
                        newBackdropHeight = firstView.getTop() + backdropHeight;
                    }

                    newBackdropHeight = Math.max(newBackdropHeight, toolbarHeight);
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) backdrop.getLayoutParams();
                    lp.height = newBackdropHeight;
                    // another option is to use y translation to not do parallax
                    backdrop.setLayoutParams(lp);
                    if (newBackdropHeight / (float)backdropHeight < .5f) {
                        toolbarFadeToPrimary();
                    } else {
                        toolbarFadeToTranslucent();
                    }
                }

                if (firstVisibleItem == 0) {
                    int remainder = firstView.getHeight() + firstView.getTop();
                    // if there's less than toolbar height left, start scrolling off.
                    if (remainder < toolbarHeight) {
                        remainder = toolbarHeight - remainder;
                        toolbarContainer.setTranslationY(-remainder);
                        if (backdrop != null)
                            backdrop.setTranslationY(-remainder);
                    } else {
                        toolbarContainer.setTranslationY(0);
                        if (backdrop != null)
                            backdrop.setTranslationY(0);
                    }
                    return;
                }

                toolbarContainer.setTranslationY(-toolbarHeight);
                if (backdrop != null)
                    backdrop.setTranslationY(-toolbarHeight);
            }
        });
    }

    boolean isPrimary = true;
    int colorPrimary;
    int colorPrimaryDark;
    int colorFaded;
    int currentStatusBarColor;
    ObjectAnimator existingToolbarAnimation;
    ValueAnimator existingStatusBarAnimation;

    void toolbarFadeToPrimary() {
        if (isPrimary)
            return;
        toolbarFadeToColor(colorPrimary);
        isPrimary = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            statusBarFadeToColor(colorPrimaryDark);
    }

    void toolbarFadeToTranslucent() {
        if (!isPrimary)
            return;
        toolbarFadeToColor(colorFaded);
        isPrimary = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            statusBarFadeToColor(0x9A000000);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void statusBarFadeToColor(int color) {
        existingStatusBarAnimation = WindowChromeUtils.statusBarFadeToColor(getContext(), existingStatusBarAnimation, color);
        if (existingStatusBarAnimation == null)
            return;
        existingStatusBarAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentStatusBarColor = (int) animation.getAnimatedValue();
            }
        });
    }

    void toolbarFadeToColor(int color) {
        if (existingToolbarAnimation != null)
            existingToolbarAnimation.cancel();
        View toolbarContainer = getChildAt(getChildCount() - 1);
        int existingColor = ((ColorDrawable)toolbarContainer.getBackground()).getColor();
        existingToolbarAnimation = ObjectAnimator.ofInt(toolbarContainer, "backgroundColor", existingColor, color);
        existingToolbarAnimation.setEvaluator(new ArgbEvaluator());
        existingToolbarAnimation.setDuration(500);
        existingToolbarAnimation.start();
    }
}
