package com.huy.library.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/21
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class AppTextView : AppCompatTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : super(context, attrs, defStyle)

    init {
        if (mTypeface == null) mTypeface = Typeface.createFromAsset(context.assets, PATH)
        this.typeface = mTypeface
    }

    companion object {

        private const val PATH = "roboto_regular.ttf"

        private var mTypeface: Typeface? = null
    }

}