package com.example.library.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import androidx.viewpager.widget.ViewPager

class AppViewPager : ViewPager {

    var swipeEnabled: Boolean = true

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        setMySwipe()
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return try {
            if (swipeEnabled) super.onInterceptTouchEvent(event)
            else false
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (swipeEnabled) super.onTouchEvent(event)
        else false
    }

    override fun onFilterTouchEventForSecurity(event: MotionEvent?): Boolean {
        return try {
            super.onFilterTouchEventForSecurity(event)
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    override fun onInterceptHoverEvent(event: MotionEvent?): Boolean {
        return try {
            super.onInterceptHoverEvent(event)
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    //down one is added for smooth swipe
    private fun setMySwipe() {

        try {
            val viewpager = ViewPager::class.java
            val scroller = viewpager.getDeclaredField("mScroller")
            scroller.isAccessible = true
            scroller.set(this, MySwipe(context))
            swipeEnabled = true

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    inner class MySwipe(context: Context) : Scroller(context, DecelerateInterpolator()) {

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/)
        }
    }
}