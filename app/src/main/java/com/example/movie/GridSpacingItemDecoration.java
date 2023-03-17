package com.example.movie;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by mrshi on 01-Nov-17.
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacingHorizontal;
    private int spacingVertical;
    private boolean includeEdgeHorizontal;
    private boolean includeEdgeVertical;

    public GridSpacingItemDecoration(int spanCount, int spacingHorizontal, int spacingVertical, boolean includeEdgeHorizontal, boolean includeEdgeVertical) {
        this.spanCount = spanCount;
        this.spacingHorizontal = spacingHorizontal;
        this.spacingVertical = spacingVertical;
        this.includeEdgeHorizontal = includeEdgeHorizontal;
        this.includeEdgeVertical = includeEdgeVertical;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdgeHorizontal) {
            outRect.left = spacingHorizontal - column * spacingHorizontal / spanCount; // spacingHorizontal - column * ((1f / spanCount) * spacingHorizontal)
            outRect.right = (column + 1) * spacingHorizontal / spanCount; // (column + 1) * ((1f / spanCount) * spacingHorizontal)
        } else {
            outRect.left = column * spacingHorizontal / spanCount; // column * ((1f / spanCount) * spacingHorizontal)
            outRect.right = spacingHorizontal - (column + 1) * spacingHorizontal / spanCount; // spacingHorizontal - (column + 1) * ((1f /    spanCount) * spacingHorizontal)

        }
        if (includeEdgeVertical) {
            if (position < spanCount) { // top edge
                outRect.top = spacingVertical;
            }
            outRect.bottom = spacingVertical; // item bottom
        } else {
            if (position >= spanCount) {
                outRect.top = spacingVertical; // item top
            }
        }
    }
}