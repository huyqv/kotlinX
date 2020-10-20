package com.example.kotlin.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.example.kotlin.R
import com.example.library.widget.AppCustomView
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

    /**
     * [AppCustomView] override
     */
    override val titleView: AppCompatTextView? get() = inputTextViewTitle

    override val errorView: AppCompatTextView? get() = inputTextViewError

    override val editView: AppCompatEditText? get() = inputEditText

    override fun layoutResource(): Int {
        return R.layout.widget_input_center
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

}