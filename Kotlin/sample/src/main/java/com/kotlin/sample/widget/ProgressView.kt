package com.kotlin.app.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.kotlin.app.R
import com.example.library.widget.AppCustomView

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/03/24
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ProgressView : AppCustomView {

    /**
     * [AppCustomView] override
     */
    override fun layoutResource(): Int {
        return R.layout.widget_progress
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun onInitialize(context: Context, types: TypedArray) {
    }

}