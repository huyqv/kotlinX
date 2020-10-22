package com.example.library.extension

import android.os.SystemClock
import android.view.View

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/21
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */

abstract class ViewClickListener : View.OnClickListener {

    private var lastClickTime: Long = 0

    private var lastClickViewId: Int = -1

    abstract fun onClicks(v: View?)

    final override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - lastClickTime > 500 || v?.id != lastClickViewId) {
            lastClickViewId = v?.id ?: -1
            onClicks(v)
        }
        lastClickTime = SystemClock.elapsedRealtime()
    }

}

fun View.addViewClickListener(block: (View?) -> Unit) {
    setOnClickListener(object : ViewClickListener() {
        override fun onClicks(v: View?) {
            block(v)
        }
    })
}

abstract class FastClickListener(private val clickCount: Int) : View.OnClickListener {

    private var lastClickTime: Long = 0

    private var currentClickCount: Int = 0

    abstract fun onViewClick(v: View?)

    final override fun onClick(v: View?) {
        if (System.currentTimeMillis() - lastClickTime > 500 || currentClickCount >= clickCount) {
            currentClickCount = 0
        }
        lastClickTime = System.currentTimeMillis()
        currentClickCount++
        if (currentClickCount == clickCount) {
            lastClickTime = 0
            currentClickCount = 0
            onViewClick(v)
        }
    }

}

fun View.addFastClickListener(clickCount: Int, block: () -> Unit) {
    setOnClickListener(object : FastClickListener(clickCount) {
        override fun onViewClick(v: View?) {
            block()
        }
    })
}