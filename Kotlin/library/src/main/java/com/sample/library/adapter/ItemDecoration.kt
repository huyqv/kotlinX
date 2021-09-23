package com.sample.library.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration(
    private val margin: Int = 0,
    private val orientation: Int = RecyclerView.VERTICAL,
    private val spanCount: Int = 1,
) : RecyclerView.ItemDecoration() {


    private var hasLeftSpacing = false

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val context = parent.context
        val divider = getDivider(context)
        when (orientation) {
            RecyclerView.VERTICAL -> {
                outRect.set(0, 0, 0, divider.intrinsicHeight)
            }
            RecyclerView.HORIZONTAL -> {
                outRect.set(0, 0, divider.intrinsicWidth, 0)
            }
        }
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        when (orientation) {
            RecyclerView.VERTICAL -> {
                drawVertical(canvas, parent)
            }
            RecyclerView.HORIZONTAL -> {
                drawHorizontal(canvas, parent)
            }
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {

        val context = parent.context
        val divider = getDivider(context)
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight
            divider.setBounds(
                left + dpToPixel(context, margin),
                top,
                right - dpToPixel(context, margin),
                bottom
            )
            divider.draw(canvas)
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {

        val context = parent.context
        val divider = getDivider(context)
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + divider.intrinsicHeight
            divider.setBounds(
                left,
                top + dpToPixel(context, margin),
                right,
                bottom - dpToPixel(context, margin)
            )
            divider.draw(canvas)
        }
    }

    private fun addGridOffset(outRect: Rect, view: View, parent: RecyclerView) {

        val frameWidth = ((parent.width - margin.toFloat() * (spanCount - 1)) / spanCount).toInt()
        val padding = parent.width / spanCount - frameWidth
        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).absoluteAdapterPosition
        outRect.top = if (itemPosition >= spanCount) margin else 0
        when {
            itemPosition % spanCount == 0 -> {
                outRect.left = 0
                outRect.right = padding
                hasLeftSpacing = true
            }
            (itemPosition + 1) % spanCount == 0 -> {
                hasLeftSpacing = false
                outRect.right = 0
                outRect.left = padding
            }
            hasLeftSpacing -> {
                hasLeftSpacing = false
                outRect.left = margin - padding
                outRect.right = if ((itemPosition + 2) % spanCount == 0) margin - padding
                else margin / 2
            }
            (itemPosition + 2) % spanCount == 0 -> {
                hasLeftSpacing = false
                outRect.left = margin / 2
                outRect.right = margin - padding
            }
            else -> {
                hasLeftSpacing = false
                outRect.left = margin / 2
                outRect.right = margin / 2
            }
        }
        outRect.bottom = 0
    }

    private fun dpToPixel(context: Context, dp: Int): Int {

        val resources = context.resources
        val dimen = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources?.displayMetrics
        )
        return Math.round(dimen) + 0
    }

    private fun getDivider(context: Context): Drawable {
        val attrArray = intArrayOf(android.R.attr.listDivider)
        val attr = context.obtainStyledAttributes(attrArray)
        return attr.getDrawable(0)!!
    }
}