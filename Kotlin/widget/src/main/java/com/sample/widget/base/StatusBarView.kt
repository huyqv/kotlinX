package com.sample.widget.base

import android.content.Context
import android.util.AttributeSet
import android.view.View

class StatusBarView : View {

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == VISIBLE) {
            val lp = this.layoutParams
            lp.height = statusBarHeight
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