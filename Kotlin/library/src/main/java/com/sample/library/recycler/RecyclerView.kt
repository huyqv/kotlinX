package com.sample.library.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.DimenRes
import androidx.annotation.IntDef
import androidx.recyclerview.widget.*
import androidx.viewbinding.ViewBinding

class BaseViewHolder : RecyclerView.ViewHolder {
    constructor(v: View) : super(v)
}

class ItemOptions(val layoutId: Int = 1, val inflaterInvoker: (View) -> ViewBinding)

typealias InflaterInvoker = (LayoutInflater, ViewGroup, Boolean) -> ViewBinding

fun InflaterInvoker?.invokeItem(parent: ViewGroup): ViewBinding? {
    return this?.invoke(LayoutInflater.from(parent.context), parent, false)
}

class GoneViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(View(parent.context).also { it.visibility = View.GONE })

class RtlGridLayoutManager : GridLayoutManager {

    constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(context: Context?, spanCount: Int = 1, orientation: Int = RecyclerView.VERTICAL, reverseLayout: Boolean = false) : super(context, spanCount, orientation, reverseLayout)

    override fun isLayoutRTL(): Boolean {
        return true
    }
}

open class DiffItemCallback<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
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

class ItemDecoration(
    private val margin: Int = 0,
    @Orientation val orientation: Int = VERTICAL,
    private val column: Int = 1,
) : RecyclerView.ItemDecoration() {

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
        const val GRID = 2
    }

    @IntDef(HORIZONTAL, VERTICAL, GRID)
    private annotation class Orientation

    private var hasLeftSpacing = false

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val context = parent.context
        if (orientation == GRID) {
            addGridOffset(outRect, view, parent)
            return
        }

        val divider = getDivider(context)
        if (orientation == VERTICAL)
            outRect.set(0, 0, 0, divider.intrinsicHeight)
        else
            outRect.set(0, 0, divider.intrinsicWidth, 0)
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        if (orientation == VERTICAL)
            drawVertical(canvas, parent)
        else if (orientation == HORIZONTAL)
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

    if (listener == null) {
        clearOnScrollListeners()
    } else {
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
                        println("Scrolled Left")
                        listener.onLeftDrag()
                    }
                    dx > 0 -> {
                        println("Scrolled Right")
                        listener.onRightDrag()
                    }
                    else -> {
                        println("No Horizontal Scrolled")
                    }
                }
                when {
                    dy < 0 -> {
                        println("Scrolled Upwards")
                        listener.onUpDrag()
                    }
                    dy > 0 -> {
                        println("Scrolled Downwards")
                        listener.onDownDrag()
                    }
                    else -> {
                        println("No Vertical Scrolled")
                    }
                }
            }
        })
}

fun RecyclerView.intLinearSnapper(onSnap: (Int) -> Unit): SnapHelper {
    this.onFlingListener = null
    val snapHelper = LinearSnapHelper()
    snapHelper.attachToRecyclerView(this)
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    val view = snapHelper.findSnapView(layoutManager) ?: return
                    val position = recyclerView.getChildAdapterPosition(view)
                    onSnap(position)
                }
                RecyclerView.SCROLL_STATE_DRAGGING -> {
                }
            }
        }
    })
    return snapHelper
}

fun RecyclerView.initLayoutManager(block: (LinearLayoutManager.() -> Unit) = {}): LinearLayoutManager {
    val lm = LinearLayoutManager(context)
    lm.block()
    layoutManager = lm
    return lm
}

fun RecyclerView.initLayoutManager(spanCount: Int, block: (GridLayoutManager.() -> Unit) = {}): GridLayoutManager {
    val lm = GridLayoutManager(context, spanCount)
    lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            adapter?.also {
                if (it.itemCount < 2 || position == it.itemCount) {
                    return lm.spanCount
                }
            }
            return 1
        }
    }
    lm.block()
    layoutManager = lm
    return lm
}

private var lastClickTime: Long = 0

private var lastClickViewId: Int = 0

abstract class ViewClickListener(private val delayedInterval: Long = 400) : View.OnClickListener {

    abstract fun onClicks(v: View?)

    private val View?.isAcceptClick: Boolean get() = this?.id != lastClickViewId && delayedInterval == 0L

    private val isDelayed: Boolean get() = System.currentTimeMillis() - lastClickTime > delayedInterval

    private var hasDelayed: Boolean = false

    final override fun onClick(v: View?) {
        if (isDelayed || v.isAcceptClick) {
            lastClickViewId = v?.id ?: -1
            lastClickTime = 0
            hasDelayed = false
            onClicks(v)
        }
        if (!hasDelayed) {
            hasDelayed = true
            lastClickTime = System.currentTimeMillis()
        }
    }
}

fun View?.addViewClickListener(delayedInterval: Long, listener: ((View?) -> Unit)? = null) {
    this ?: return
    if (listener == null) {
        setOnClickListener(null)
        if (this is EditText) {
            isFocusable = true
            isCursorVisible = true
        }
        return
    }
    setOnClickListener(object : ViewClickListener(delayedInterval) {
        override fun onClicks(v: View?) {
            listener(v)
        }
    })
    if (this is EditText) {
        isFocusable = false
        isCursorVisible = false
    }
}

fun View?.addViewClickListener(listener: ((View?) -> Unit)? = null) {
    addViewClickListener(0, listener)
}

fun RecyclerView.scrollToCenter(position: Int) {
    val smoothScroller: RecyclerView.SmoothScroller = CenterLayoutManager.CenterSmoothScroller(context)
    smoothScroller.targetPosition = position
    this.layoutManager?.startSmoothScroll(smoothScroller)
}

class CenterLayoutManager : LinearLayoutManager {

    constructor(context: Context) : super(context)

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State, position: Int) {
        val centerSmoothScroller = CenterSmoothScroller(recyclerView.context)
        centerSmoothScroller.targetPosition = position
        startSmoothScroll(centerSmoothScroller)
    }

    class CenterSmoothScroller(context: Context) : LinearSmoothScroller(context) {
        override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int = (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
    }
}