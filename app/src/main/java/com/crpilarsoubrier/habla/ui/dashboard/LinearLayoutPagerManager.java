package com.crpilarsoubrier.habla.ui.dashboard;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 *  We extend LinearLayout to allow specifying the number of visible items
 */
public class LinearLayoutPagerManager extends LinearLayoutManager {

    private int mItemsPerPage;

    public int getItemsPerPage()
    {
        return mItemsPerPage;
    }


    public LinearLayoutPagerManager(Context context, int orientation, boolean reverseLayout, int itemsPerPage) {
        super(context, orientation, reverseLayout);

        mItemsPerPage = itemsPerPage;
    }

    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        return super.checkLayoutParams(lp) && lp.width == getItemSize();
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return setProperItemSize(super.generateDefaultLayoutParams());
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return setProperItemSize(super.generateLayoutParams(lp));
    }

    private RecyclerView.LayoutParams setProperItemSize(RecyclerView.LayoutParams lp) {
        int itemSize = getItemSize();
        if (getOrientation() == HORIZONTAL) {
            lp.width = itemSize;
        } else {
            lp.height = itemSize;
        }
        return lp;
    }

    private int getItemSize() {
        int pageSize = getOrientation() == HORIZONTAL ? getWidth() : getHeight();
        return Math.round((float) pageSize / mItemsPerPage);
    }

}
