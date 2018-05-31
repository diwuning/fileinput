package com.h.fileinput.FragmentPager;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by geyuecang on 2017/9/29.
 */

public class RecyclerViewSpaceDecoration extends RecyclerView.ItemDecoration {
    private int space;
    public RecyclerViewSpaceDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        if(parent.getChildPosition(view) == 0) {
            outRect.top = space;
        }
    }
}
