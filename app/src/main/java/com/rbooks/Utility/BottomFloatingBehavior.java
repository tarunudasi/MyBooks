package com.rbooks.Utility;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.view.View;

public class BottomFloatingBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {
    private int height;

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton child, int layoutDirection) {
        height = child.getHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyConsumed > 0) {
            slideDown(child);
        } else if (dyConsumed < 0) {
            slideUp(child);
        }
    }

    private void slideUp(FloatingActionButton child) {
        child.clearAnimation();
        child.animate().translationY(0).setDuration(200);
        //child.animate().alpha(1.0f).setDuration(200);
    }

    private void slideDown(FloatingActionButton child) {
        child.clearAnimation();
        child.animate().translationY(height + height).setDuration(200);
        //child.animate().alpha(0.0f).setDuration(200);
    }

}

