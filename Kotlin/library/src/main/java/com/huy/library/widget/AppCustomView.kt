package com.huy.library.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout

abstract class AppCustomView : FrameLayout {

    protected abstract val styleRes: IntArray

    protected abstract val layoutRes: Int

    protected abstract fun onInitialize(context: Context, types: TypedArray)

    constructor(context: Context) : super(context) {
        onViewInit(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        onViewInit(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        onViewInit(context, attrs)
    }

    private fun onViewInit(context: Context, attrs: AttributeSet?) {
        val types = context.theme.obtainStyledAttributes(attrs, styleRes, 0, 0)
        try {
            LayoutInflater.from(context).inflate(layoutRes, this)
            onInitialize(context, types)
        } finally {
            types.recycle()
        }
    }

}