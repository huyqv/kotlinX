package com.sample.widget.base

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView


abstract class AppScrollView : NestedScrollView {

    var scrollable = true

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return scrollable && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return scrollable && super.onInterceptTouchEvent(ev)
    }

    abstract class SimpleScrollChangeListener : OnScrollChangeListener {

        private var scrollYPos: Int = 0

        open fun onMostTopScroll() = Unit

        override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
            if (scrollYPos != 0 && scrollY == 0) {
                scrollYPos = 0
                Log.d("HomeBottomView", "onMostTopScrolled")
                onMostTopScroll()
            }
            scrollYPos = scrollY
        }

    }
}