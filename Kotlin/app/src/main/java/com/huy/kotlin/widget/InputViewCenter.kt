package com.huy.kotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.TextView
import com.huy.kotlin.R
import kotlinx.android.synthetic.main.widget_input_center.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class InputViewCenter : CustomInputView {

    override val titleView: TextView get() = inputTextViewTitle

    override val errorView: TextView get() = inputTextViewError

    override val editView: EditText get() = inputEditText

    override val styleRes: IntArray get() = R.styleable.InputView

    override val layoutRes: Int get() = R.layout.widget_input_center

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

}