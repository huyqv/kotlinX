package com.sample.widget.base

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

class AppCoordinatorLayout(context: Context, attrs: AttributeSet) : CoordinatorLayout(context, attrs) {

    var isExpandable: Boolean = true

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int, type: Int): Boolean {
        return isExpandable && super.onStartNestedScroll(child, target, nestedScrollAxes, type)
    }

}
