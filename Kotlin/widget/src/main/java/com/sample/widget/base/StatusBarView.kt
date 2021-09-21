package com.sample.widget.base

import android.content.Context
import android.util.AttributeSet

class StatusBarView : AppExpandableLayout {

    companion object {
        private var savedStatusBarHeight: Int = 0
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        duration = 420
    }

    override fun setExpanded(expand: Boolean, animate: Boolean) {
        updateStatusBar()
        super.setExpanded(expand, animate)
    }

    private fun updateStatusBar() {
        if (savedStatusBarHeight == 0) {
            savedStatusBarHeight = statusBarHeight
        }
        val lp = this.layoutParams
        if (lp.height != savedStatusBarHeight) {
            lp.height = savedStatusBarHeight
            this.layoutParams = lp
        }
    }

    private val statusBarHeight: Int
        get() {
            val resources = context.resources
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) return resources.getDimensionPixelSize(resourceId)
            return 0
        }

}