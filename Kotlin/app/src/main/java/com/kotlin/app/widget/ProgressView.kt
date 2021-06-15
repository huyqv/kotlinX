package com.kotlin.app.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.example.library.widget.AppCustomView
import com.kotlin.app.R.layout

class ProgressView : AppCustomView {

    /**
     * [AppCustomView] override
     */
    override fun layoutResource(): Int {
        return layout.widget_progress
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun onInitialize(context: Context, types: TypedArray) {
    }

}