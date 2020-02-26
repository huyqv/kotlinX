package com.huy.kotlin.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.EditText
import android.widget.TextView
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
class InputViewIcon : CustomInputView {

    override val titleView: TextView get() = inputTextViewTitle

    override val errorView: TextView get() = inputTextViewError

    override val editView: EditText get() = inputEditText

    override val styleRes: IntArray get() = R.styleable.InputView

    override val layoutRes: Int get() = R.layout.widget_input_icon

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun configDrawable(types: TypedArray) {

        val color = getTypedColor(types, R.styleable.InputView_android_textColor)

        val drawableLeft = getTypedDrawable(types, R.styleable.InputView_android_drawableStart, color)

        val drawableRight = getTypedDrawable(types, R.styleable.InputView_android_drawableEnd, color)

        editView.post {
            editView.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null)
        }

        val drawable = getTypedDrawable(types, R.styleable.InputView_android_drawable, color)
                ?: return
        inputImageViewDrawable.setImageDrawable(drawable)

    }







}