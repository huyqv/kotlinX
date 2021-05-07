package com.example.library.extension

import android.view.View
import android.widget.EditText


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/21
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */

private var lastClickTime: Long = 0

private var lastClickViewId: Int = -1

abstract class ViewClickListener(private val delayedInterval: Long = 400) : View.OnClickListener {

    abstract fun onClicks(v: View?)

    private val View?.isAcceptClick: Boolean get() = this?.id != lastClickViewId && delayedInterval == 0L

    private val isDelayed: Boolean get() = nowInMillis - lastClickTime > delayedInterval

    private var hasDelayed: Boolean = false

    final override fun onClick(v: View?) {
        val b1 = isDelayed
        val b2 = v.isAcceptClick
        if (b1 || b2) {
            lastClickViewId = v?.id ?: -1
            lastClickTime = 0
            hasDelayed = false
            onClicks(v)
        }
        if (!hasDelayed) {
            hasDelayed = true
            lastClickTime = nowInMillis
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

fun addClickListeners(vararg views: View?, block: (View?) -> Unit) {
    val listener = object : ViewClickListener() {
        override fun onClicks(v: View?) {
            block(v)
        }
    }
    views.forEach {
        it?.setOnClickListener(listener)
    }
}

fun clearClickListeners(vararg views: View?) {
    views.forEach {
        it?.setOnClickListener(null)
    }
}

abstract class FastClickListener(private val clickCount: Int) : View.OnClickListener {

    private var lastClickTime: Long = 0

    private var currentClickCount: Int = 0

    abstract fun onViewClick(v: View?)

    final override fun onClick(v: View?) {
        if (nowInMillis - lastClickTime > 420 || currentClickCount >= clickCount) {
            currentClickCount = 0
        }
        lastClickTime = nowInMillis
        currentClickCount++
        if (currentClickCount == clickCount) {
            lastClickTime = 0
            currentClickCount = 0
            onViewClick(v)
        }
    }

}

fun View?.addFastClickListener(clickCount: Int, block: () -> Unit) {
    this?.setOnClickListener(object : FastClickListener(clickCount) {
        override fun onViewClick(v: View?) {
            block()
        }
    })
}
