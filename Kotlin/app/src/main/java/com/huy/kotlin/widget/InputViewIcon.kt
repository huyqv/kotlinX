package com.huy.kotlin.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.huy.kotlin.R
import kotlinx.android.synthetic.main.widget_input_icon.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class InputViewIcon : InputView {

    override val titleView: AppCompatTextView? get() = inputTextViewTitle

    override val errorView: AppCompatTextView? get() = inputTextViewError

    override val editView: AppCompatEditText? get() = inputEditText

    override val layoutRes: Int get() = R.layout.widget_input_icon

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun configDrawable(types : TypedArray) {

        val color = types.textColor
        val drawableLeft = types.drawableStart?.apply { setTintColor(color) }
        val drawableRight = types.drawableEnd?.apply { setTintColor(color) }
        editView?.post {
            editView?.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null)
        }

        val drawable = types.drawable?.also {
            it.setTintColor(color)
        }
        inputImageViewDrawable.setImageDrawable(drawable)

    }

}