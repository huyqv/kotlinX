package com.huy.library.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/08/29
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class AppCheckableView : AppCompatImageView {

    interface StateChangedListener {
        fun onStateChanged(v: AppCheckableView, selected: Boolean)
    }

    private var colorSelected: Int = Color.parseColor("#515151")

    private var colorUnSelected: Int = Color.parseColor("#b4b4b4")

    private var mValue = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    fun switch(value: Boolean) {
        post {
            mValue = value
            this@AppCheckableView.setColorFilter(if (mValue) colorSelected else colorUnSelected)
        }
    }

    fun setStateChangedListener(listener: StateChangedListener?) {
        setOnClickListener {
            switch(!mValue)
            listener?.onStateChanged(this, mValue)
        }
    }

}