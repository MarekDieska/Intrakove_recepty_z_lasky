package com.example.dvojplatnicka;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;
    private int staticMargin;  // Static margin for left and right// Bottom margin
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, int staticMargin, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.staticMargin = staticMargin;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        // Calculate the left and right margins
        int leftMargin = staticMargin;
        int rightMargin = staticMargin;

        // Calculate spacing for left and right of each item
        int spacingLeft = column * spacing / spanCount;
        int spacingRight = spacing - (column + 1) * spacing / spanCount;

        // Set item offsets
        if (includeEdge) {
            outRect.left = leftMargin - spacingLeft;
            outRect.right = rightMargin - spacingRight;

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
        } else {
            outRect.left = leftMargin + spacingLeft;
            outRect.right = rightMargin + spacingRight;
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}
