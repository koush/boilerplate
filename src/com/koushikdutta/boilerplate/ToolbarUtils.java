package com.koushikdutta.boilerplate;

import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

/**
 * Created by koush on 4/4/15.
 */
public final class ToolbarUtils {
    public static void enableToolbarScrollOff(final View toolbar, final AbsListView absListView, final View contentContainer) {
        absListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (absListView.getChildCount() < 1) {
                    return;
                }

                final View firstView = absListView.getChildAt(0);

                final int toolbarHeight = toolbar.getHeight();
                float percent;
                if (firstVisibleItem >= 1) {
                    percent = 1f;
                }
                else {
                    int y = -firstView.getTop();
                    y += (firstView.getHeight() * firstVisibleItem);
                    percent = y / (float) (firstView.getHeight() );
                }

                ViewGroup.MarginLayoutParams tlp = (ViewGroup.MarginLayoutParams)contentContainer.getLayoutParams();
                if (firstVisibleItem == 0) {
                    int remainder = firstView.getHeight() + firstView.getTop();
                    // if there's less than toolbar height left, start scrolling off.
                    if (remainder < toolbarHeight) {
                        remainder = toolbarHeight - remainder;
                        tlp.topMargin = -remainder;
                        contentContainer.setLayoutParams(tlp);
                    }
                    else {
                        tlp.topMargin = 0;
                        contentContainer.setLayoutParams(tlp);
                    }
                    return;
                }

                tlp.topMargin = -toolbarHeight;
                contentContainer.setLayoutParams(tlp);
            }
        });
    }
}
