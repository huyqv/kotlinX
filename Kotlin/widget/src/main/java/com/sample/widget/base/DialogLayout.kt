package com.sample.widget.base

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class DialogLayout : ConstraintLayout {

    companion object {
        val backgroundColor = Color.parseColor("#CD474747")
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        setBackgroundColor(backgroundColor)
        when (context) {
            is androidx.appcompat.view.ContextThemeWrapper, is android.view.ContextThemeWrapper -> {
                setPadding(0, statusBarHeight, 0, 0)
            }
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
