package com.sample.library.adapter

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class GridDecoration : RecyclerView.ItemDecoration {

    private val spanCount: Int

    private val spacing: Int

    private val includeEdge: Boolean

    constructor(context: Context, spanCount: Int, dp: Int, includeEdge: Boolean) {

        val resources = context.resources
        val metrics = resources.displayMetrics

        this.spanCount = spanCount
        this.spacing = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        this.includeEdge = includeEdge
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        if (includeEdge) {

            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount
            if (position < spanCount) outRect.top = spacing
            outRect.bottom = spacing
        } else {

            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) outRect.top = spacing
        }
    }

    companion object {

        fun drawByRes(
            recycler: RecyclerView,
            col: Int,
            @DimenRes dimenRes: Int,
            includeEdge: Boolean = false
        ) {
            val dp = recycler.resources.getDimensionPixelSize(dimenRes)
            draw(recycler, col, dp, includeEdge)
        }

        fun draw(recycler: RecyclerView, col: Int, dp: Int, includeEdge: Boolean = false) {
            recycler.addItemDecoration(GridDecoration(recycler.context, col, dp, includeEdge))
        }
    }
}