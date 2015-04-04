package com.koushikdutta.boilerplate;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by koush on 3/30/15.
 */
public abstract class IconListDrawerActivity extends ActionBarActivity implements AbsListView.OnScrollListener, FragmentManager.OnBackStackChangedListener {
    Toolbar toolbar;
    View toolbarContainer;
    ActionBarDrawerToggle drawerToggle;
    LayerDrawable toolbarFade;
    View backdrop;
    int colorPrimary;
    int colorFaded;
    boolean isPrimary = true;
    ObjectAnimator existingToolbarAnimation;

    public AbsListView.OnScrollListener getBackdropScrollListener() {
        return this;
    }

    public interface BackdropTransitionCallback {
        void onCompleted();
    }

    public void performBackdropTransition(final View source, final BackdropTransitionCallback callback) {
        final ViewGroup c = (ViewGroup)findViewById(R.id.toolbar_content_container);
        int[] sourceLocation = new int[2];
        source.getLocationInWindow(sourceLocation);
        int[] containerLocation = new int[2];
        c.getLocationInWindow(containerLocation);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(source.getWidth(), source.getHeight());
        lp.leftMargin = sourceLocation[0] - containerLocation[0];
        lp.topMargin = sourceLocation[1] - containerLocation[1];
        final ViewGroup sp = (ViewGroup)source.getParent();
        final int index = sp.indexOfChild(source);
        ((ViewGroup) source.getParent()).removeView(source);
        final ViewGroup.LayoutParams original = source.getLayoutParams();
        source.setLayoutParams(lp);
        c.addView(source);


        c.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                c.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                source.post(new Runnable() {
                    @Override
                    public void run() {
                        int finalRadius = (int)Math.sqrt(backdrop.getWidth() * backdrop.getWidth() + backdrop.getHeight() * backdrop.getHeight()) ;
                        int initialRadius = (int)Math.sqrt(source.getWidth() * source.getWidth() + source.getHeight() * source.getHeight()) / 4;

                        final float alpha = source.getAlpha();
                        Animator anim =
                        ViewAnimationUtils.createCircularReveal(source, source.getWidth() / 2, source.getHeight() / 2, initialRadius, finalRadius);
                        anim.setDuration(800);
                        anim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                source.animate()
                                .alpha(0)
                                .setDuration(500)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        onAnimationCancel(null);
                                    }
                                });
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                                c.removeView(source);
                                source.setAlpha(alpha);
                                source.setLayoutParams(original);
                                sp.addView(source, index);
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        anim.start();

                        AutoTransition t = new AutoTransition();
                        t.addListener(new Transition.TransitionListener() {
                            @Override
                            public void onTransitionStart(Transition transition) {

                            }

                            @Override
                            public void onTransitionEnd(Transition transition) {
                                callback.onCompleted();
                            }

                            @Override
                            public void onTransitionCancel(Transition transition) {

                            }

                            @Override
                            public void onTransitionPause(Transition transition) {

                            }

                            @Override
                            public void onTransitionResume(Transition transition) {

                            }
                        });
                        t.setDuration(300);
                        TransitionManager.beginDelayedTransition(c, t);
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.icon_list_drawer_activity_backdrop_height));
                        source.setLayoutParams(lp);
                    }
                });
            }
        });
    }

    private final Object HEADER_VIEW = new Object();
    public View getBackdropListViewHeader() {
        View ret = getLayoutInflater().inflate(R.layout.icon_list_drawer_activity_listview_header, null);
        ret.setTag(HEADER_VIEW);
        View v = ret.findViewById(R.id.icon_list_drawer_activity_listview_header);
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.height = getResources().getDimensionPixelSize(R.dimen.icon_list_drawer_activity_backdrop_height);// - toolbarContainer.getHeight();
        v.setLayoutParams(lp);

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_content);
        ViewGroup.MarginLayoutParams flp = (ViewGroup.MarginLayoutParams)f.getView().getLayoutParams();
        flp.topMargin = -toolbarContainer.getHeight();
        f.getView().setLayoutParams(flp);
        return ret;
    }

    @Override
    public void onBackStackChanged() {
        Fragment backdrop = getSupportFragmentManager().findFragmentById(R.id.backdrop_container);
        if (backdrop == null || !backdrop.isAdded())
            toolbarFadeToPrimary();
        else
            toolbarFadeToTranslucent();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (absListView.getChildCount() < 1) {
            return;
        }

        final View firstView = absListView.getChildAt(0);

        final int toolbarHeight = toolbarContainer.getHeight();
        float percent;
        if (firstVisibleItem >= 1) {
            percent = 1f;
        }
        else {
            int y = -firstView.getTop();
            y += (firstView.getHeight() * firstVisibleItem);
            percent = y / (float) (firstView.getHeight() );
        }

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)backdrop.getLayoutParams();
        lp.height = (int)((1f - percent) * getResources().getDimensionPixelSize(R.dimen.icon_list_drawer_activity_backdrop_height));
        lp.height = Math.max(lp.height, toolbarHeight);
        lp.topMargin = 0;
        backdrop.setLayoutParams(lp);
        if (percent > .5f) {
            toolbarFadeToPrimary();
        }
        else {
            toolbarFadeToTranslucent();
        }

        FrameLayout.LayoutParams tlp = (FrameLayout.LayoutParams)toolbarContainer.getLayoutParams();
        if (firstVisibleItem == 0) {
            int remainder = firstView.getHeight() + firstView.getTop();
            // if there's less than toolbar height left, start scrolling off.
            if (remainder < toolbarHeight) {
                remainder = toolbarHeight - remainder;
                tlp.topMargin = -remainder;
                toolbarContainer.setLayoutParams(tlp);

                lp.topMargin = -remainder;
                backdrop.setLayoutParams(lp);
            }
            else {
                tlp.topMargin = 0;
                toolbarContainer.setLayoutParams(tlp);

                lp.topMargin = 0;
                backdrop.setLayoutParams(lp);
            }
            return;
        }

        tlp.topMargin = -toolbarHeight;
        toolbarContainer.setLayoutParams(tlp);

        lp.topMargin = -toolbarHeight;
        backdrop.setLayoutParams(lp);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void goFullScreenStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    void toolbarFadeToPrimary() {
        if (isPrimary)
            return;
        toolbarFadeToColor(colorPrimary);
        isPrimary = true;
    }

    void toolbarFadeToTranslucent() {
        if (!isPrimary)
            return;
        toolbarFadeToColor(colorFaded);
        isPrimary = false;
    }

    void toolbarFadeToColor(int color) {
        if (existingToolbarAnimation != null)
            existingToolbarAnimation.cancel();
        int existingColor = ((ColorDrawable)toolbarContainer.getBackground()).getColor();
        existingToolbarAnimation = ObjectAnimator.ofInt(toolbarContainer, "backgroundColor", existingColor, color);
        existingToolbarAnimation.setEvaluator(new ArgbEvaluator());
        existingToolbarAnimation.setDuration(500);
        existingToolbarAnimation.start();
    }

    public Fragment getDrawerFragment() {
        return getSupportFragmentManager().findFragmentByTag("drawer");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            goFullScreenStatusBar();

        setContentView(R.layout.icon_list_drawer_activity);
        setSupportActionBar(toolbar = ((Toolbar) findViewById(R.id.toolbar)));

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        backdrop = findViewById(R.id.backdrop_container);
        toolbarContainer = findViewById(R.id.toolbar_container);
        colorPrimary = TintHelper.getColorPrimary(this);
        colorFaded = 0;
        toolbarFade = new LayerDrawable(new Drawable[] { getResources().getDrawable(R.drawable.icon_list_drawer_activity_toolbar_gradient_translucent), new ColorDrawable(TintHelper.getColorPrimary(this)) });

        if (savedInstanceState == null) {
            Fragment drawer = createDrawerFragment();
            getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.drawer_content, drawer, "drawer")
            .commitAllowingStateLoss();
        }

        getDrawer()
        .setDrawerListener(drawerToggle = new ActionBarDrawerToggle(this, getDrawer(), getDrawerOpenString(), getDrawerCloseString()) {
            DecelerateInterpolator interpolator = new DecelerateInterpolator();

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                View content = getDrawer().getChildAt(0);
                View drawer = getDrawer().getChildAt(1);
                content.setTranslationX(Math.min(content.getMeasuredWidth(), drawer.getMeasuredWidth()) / 3 * interpolator.getInterpolation(slideOffset));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (isFinishing())
                    return;
                IconListDrawerActivity.this.onDrawerClosed();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
        onDrawerReady();
    }

    protected void onDrawerReady() {
    }

    protected void onDrawerClosed() {
    }

    public abstract int getDrawerOpenString();

    public abstract int getDrawerCloseString();

    protected abstract Fragment createDrawerFragment();

    public Fragment getCurrentContentFragment() {
        Fragment ret = getSupportFragmentManager().findFragmentById(R.id.main_content);
        if (ret != null && ret.isAdded())
            return ret;
        return null;
    }

    public FragmentTransaction beginContentFragmentTransaction(final Fragment content, final String breadcrumb, final @Nullable Fragment backdrop) {
        FragmentTransaction ft =
        getSupportFragmentManager()
        .beginTransaction();

        if (breadcrumb != null && null != getSupportFragmentManager().findFragmentById(R.id.main_content))
            ft.addToBackStack(breadcrumb);
        if (backdrop != null) {
            ft.replace(R.id.backdrop_container, backdrop);
        }
        else {
            Fragment existingBackdrop = getSupportFragmentManager().findFragmentById(R.id.backdrop_container);
            if (existingBackdrop != null && existingBackdrop.isAdded())
                ft.remove(existingBackdrop);
        }
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.main_content, content);
        return ft;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public DrawerLayout getDrawer() {
        return (DrawerLayout)findViewById(R.id.drawer_layout);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getDrawer().isDrawerOpen(Gravity.LEFT)) {
            getDrawer().closeDrawers();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
