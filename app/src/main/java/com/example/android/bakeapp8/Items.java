package com.example.android.bakeapp8;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;


public class Items extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public Items(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect rect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;

        if (includeEdge) {
            rect.left = spacing - column * spacing / spanCount;
            rect.right = (column + 1) * spacing / spanCount;

            if (position < spanCount) {
                rect.top = spacing;
            }
            rect.bottom = spacing;
        } else {
            rect.left = column * spacing / spanCount;
            rect.right = spacing - (column + 1) * spacing / spanCount;
            if (position >= spanCount) {
                rect.top = spacing;
            }
        }
    }

    public static int PxConverter(Context context, int dp) {
        Resources resources = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics()));
    }
}


