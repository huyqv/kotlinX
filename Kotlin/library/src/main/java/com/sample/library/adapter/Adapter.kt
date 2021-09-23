package com.sample.library.adapter

import android.app.Activity
import android.content.ContextWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.viewbinding.ViewBinding
import com.sample.library.extension.screenHeight


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

fun RecyclerView.scrollToCenter(position: Int) {
    val smoothScroller: RecyclerView.SmoothScroller =
        CenterLayoutManager.CenterSmoothScroller(context)
    smoothScroller.targetPosition = position
    this.layoutManager?.startSmoothScroll(smoothScroller)
}

fun RecyclerView.setItemAppearAnimation(gravity: Int = Gravity.TOP) {
    val screenHeight = this.requireActivity()?.screenHeight?.toFloat() ?: this.bottom.toFloat()
    val set = AnimationSet(true)
    set.addAnimation(AlphaAnimation(0.0F, 1.0F).also {
        it.duration = 600
        it.fillAfter = true
    })
    set.addAnimation(TranslateAnimation(0F, 0F, screenHeight, 0F).also {
        it.interpolator = DecelerateInterpolator(4F)
        it.duration = 600
    })
    layoutAnimation = LayoutAnimationController(set, 0.2F)
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

fun View.requireActivity(): Activity? {
    val lifecycleOwner = this.findViewTreeLifecycleOwner()
    if (lifecycleOwner is Activity) return lifecycleOwner
    if (lifecycleOwner is Fragment) return lifecycleOwner.requireActivity()
    var context = context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}


