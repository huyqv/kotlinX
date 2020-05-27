package com.huy.library.adapter.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/11/02
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface ScrollListener {

    fun onScrolling()

    fun onStopScroll()
}

interface MostScrollListener {

    fun onMostTopScrolled()

    fun onMostBottomScrolled()
}

interface DragListener {

    fun onLeftDrag()

    fun onRightDrag()

    fun onUpDrag()

    fun onDownDrag()
}

fun RecyclerView.addScrollListener(listener: ScrollListener?) {

    if (listener == null)
        clearOnScrollListeners()
    else
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        println("Not scrolling")
                        listener.onStopScroll()
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        println("Scrolling now")
                        listener.onScrolling()
                    }
                }
            }
        })
}

fun RecyclerView.addMostScrollListener(listener: MostScrollListener?) {

    if (listener == null)
        clearOnScrollListeners()
    else
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (layoutManager is LinearLayoutManager) {
                    val pastVisibleItems: Int = (layoutManager as LinearLayoutManager)
                            .findFirstCompletelyVisibleItemPosition()
                    if (pastVisibleItems == 0)
                        listener.onMostTopScrolled()
                }
            }
        })
}

fun RecyclerView.addDragListener(listener: DragListener?) {

    if (listener == null)
        clearOnScrollListeners()
    else
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                when {
                    dx < 0 -> {
                        System.out.println("Scrolled Left")
                        listener.onLeftDrag()
                    }
                    dx > 0 -> {
                        System.out.println("Scrolled Right")
                        listener.onRightDrag()
                    }
                    else -> {
                        System.out.println("No Horizontal Scrolled")
                    }
                }
                when {
                    dy < 0 -> {
                        System.out.println("Scrolled Upwards")
                        listener.onUpDrag()
                    }
                    dy > 0 -> {
                        System.out.println("Scrolled Downwards")
                        listener.onDownDrag()
                    }
                    else -> {
                        System.out.println("No Vertical Scrolled")
                    }
                }
            }
        })
}

open class DiffItemCallback<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return false
    }
}

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

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

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

        fun drawByRes(recycler: RecyclerView, col: Int, @DimenRes dimenRes: Int, includeEdge: Boolean = false) {
            val dp = recycler.resources.getDimensionPixelSize(dimenRes)
            draw(recycler, col, dp, includeEdge)
        }

        fun draw(recycler: RecyclerView, col: Int, dp: Int, includeEdge: Boolean = false) {
            recycler.addItemDecoration(GridDecoration(recycler.context, col, dp, includeEdge))
        }
    }

}

class SeparateDecoration : RecyclerView.ItemDecoration {

    enum class Layout {
        HORIZONTAL, VERTICAL, GRID
    }

    private var margin: Int = 0

    private var orientation: Layout = Layout.VERTICAL

    private var column: Int = 1

    private var hasLeftSpacing = false

    constructor(margin: Int, column: Int) {

        this.margin = margin
        this.column = column
        this.orientation = Layout.GRID
    }

    constructor(margin: Int = 0, orientation: Layout = Layout.VERTICAL) {
        this.margin = margin
        this.orientation = orientation
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val context = parent.context
        if (orientation == Layout.GRID) {
            addGridOffset(outRect, view, parent)
            return
        }

        val divider = getDivider(context)
        if (orientation == Layout.VERTICAL)
            outRect.set(0, 0, 0, divider.intrinsicHeight)
        else
            outRect.set(0, 0, divider.intrinsicWidth, 0)
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        if (orientation == Layout.VERTICAL)
            drawVertical(canvas, parent)
        else if (orientation == Layout.HORIZONTAL)
            drawHorizontal(canvas, parent)
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
            divider.setBounds(left + dpToPixel(context, margin), top, right - dpToPixel(context, margin), bottom)
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
            divider.setBounds(left, top + dpToPixel(context, margin), right, bottom - dpToPixel(context, margin))
            divider.draw(canvas)
        }
    }

    private fun addGridOffset(outRect: Rect, view: View, parent: RecyclerView) {

        val frameWidth = ((parent.width - margin.toFloat() * (column - 1)) / column).toInt()
        val padding = parent.width / column - frameWidth
        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        outRect.top = if (itemPosition >= column) margin else 0
        when {
            itemPosition % column == 0 -> {
                outRect.left = 0
                outRect.right = padding
                hasLeftSpacing = true
            }
            (itemPosition + 1) % column == 0 -> {
                hasLeftSpacing = false
                outRect.right = 0
                outRect.left = padding
            }
            hasLeftSpacing -> {
                hasLeftSpacing = false
                outRect.left = margin - padding
                outRect.right = if ((itemPosition + 2) % column == 0) margin - padding
                else margin / 2
            }
            (itemPosition + 2) % column == 0 -> {
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
        val dimen = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources?.displayMetrics)
        return Math.round(dimen) + 0
    }

    private fun getDivider(context: Context): Drawable {
        val attrArray = intArrayOf(android.R.attr.listDivider)
        val attr = context.obtainStyledAttributes(attrArray)
        return attr.getDrawable(0)!!
    }

}