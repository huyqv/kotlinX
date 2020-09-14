package com.huy.kotlin.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.huy.kotlin.R
import com.huy.library.widget.AppCustomView

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
     * [AppCustomView] implement
     */
    override val layoutRes: Int get() = R.layout.widget_progress

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onInitialize(context: Context, types: TypedArray) {
    }

}