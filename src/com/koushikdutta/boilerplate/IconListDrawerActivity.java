package com.koushikdutta.boilerplate;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by koush on 3/30/15.
 */
public abstract class IconListDrawerActivity extends ActionBarActivity {
    ActionBarDrawerToggle drawerToggle;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void goTranslucentStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void goFullscreenLayout() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    public Fragment getDrawerFragment() {
        return getSupportFragmentManager().findFragmentByTag("drawer");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            goFullscreenLayout();
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            goTranslucentStatusBar();

        setContentView(R.layout.icon_list_drawer_activity);

        if (savedInstanceState == null) {
            Fragment drawer = createDrawerFragment();
            getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.drawer_content, drawer, "drawer")
            .commitAllowingStateLoss();
        }

        resetDrawerToggle();
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        resetDrawerToggle();
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    private void resetDrawerToggle() {
        getDrawer()
        .setDrawerListener(drawerToggle = new ActionBarDrawerToggle(this, getDrawer(), getDrawerOpenString(), getDrawerCloseString()) {
            DecelerateInterpolator interpolator = new DecelerateInterpolator();

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                // this check exists in case we want to animate the drawer icon into a back indicator
                // and don't erroneously translate this stuff.
                if (!getDrawer().isDrawerOpen(Gravity.LEFT))
                    return;
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
        drawerToggle.syncState();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
        if (savedInstanceState == null)
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

    public FragmentTransaction beginContentFragmentTransaction(final Fragment content, final String breadcrumb) {
        FragmentTransaction ft =
        getSupportFragmentManager()
        .beginTransaction();

        if (breadcrumb != null && null != getSupportFragmentManager().findFragmentById(R.id.main_content))
            ft.addToBackStack(breadcrumb);
//        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.main_content, content);
        return ft;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item) )
            return true;

        if (item.getItemId() == item.getItemId()) {
            getSupportFragmentManager().popBackStackImmediate();
            return true;
        }

        return super.onOptionsItemSelected(item);
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