package com.huy.kotlin.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout

abstract class CustomView : FrameLayout {

    protected abstract val styleRes: IntArray

    protected abstract val layoutRes: Int

    protected abstract fun onInitialize(context: Context, types: TypedArray)

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val types = context.theme.obtainStyledAttributes(attrs, styleRes, 0, 0)
        try {
            LayoutInflater.from(context).inflate(layoutRes, this)
            onInitialize(context, types)
        } finally {
            types.recycle()
        }
    }

}