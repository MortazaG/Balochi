package com.alchemistmoz.balochi.misc;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * Customized to disable scrolling for games.
 */
public class CustomGridLayoutManager extends GridLayoutManager {

    public CustomGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    /**
     * Disable scrolling by returning false.
     */
    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
