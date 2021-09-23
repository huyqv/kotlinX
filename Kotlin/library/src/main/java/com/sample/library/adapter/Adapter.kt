package com.sample.library.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.viewbinding.ViewBinding


class ItemOptions(val layoutId: Int = 1, val inflaterInvoker: (View) -> ViewBinding)

typealias InflaterInvoker = (LayoutInflater, ViewGroup, Boolean) -> ViewBinding

fun InflaterInvoker?.invokeItem(parent: ViewGroup): ViewBinding? {
    return this?.invoke(LayoutInflater.from(parent.context), parent, false)
}

class BaseViewHolder : RecyclerView.ViewHolder {
    constructor(parent: ViewGroup, @LayoutRes layoutId: Int) :
            super(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
}

class GoneViewHolder : RecyclerView.ViewHolder {
    constructor(parent: ViewGroup) :
            super(View(parent.context).also { it.visibility = View.GONE })
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
        return
    }
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
    if (listener == null) {
        clearOnScrollListeners()
        return
    }
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
    if (listener == null) {
        clearOnScrollListeners()
        return
    }
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

fun RecyclerView.scrollToCenter(position: Int) {
    val smoothScroller: RecyclerView.SmoothScroller =
        CenterLayoutManager.CenterSmoothScroller(context)
    smoothScroller.targetPosition = position
    this.layoutManager?.startSmoothScroll(smoothScroller)
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


