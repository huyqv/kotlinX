package com.huy.kotlin.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
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
class InputViewCenter : InputView {

    override val titleView: AppCompatTextView? get() = inputTextViewTitle

    override val errorView: AppCompatTextView? get() = inputTextViewError

    override val editView: AppCompatEditText? get() = inputEditText

    override val layoutRes: Int get() = R.layout.widget_input_center

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

}