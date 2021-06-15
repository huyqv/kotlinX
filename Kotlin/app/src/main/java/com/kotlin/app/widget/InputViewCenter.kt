package com.kotlin.app.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.example.library.widget.AppCustomView
import com.kotlin.app.R
import kotlinx.android.synthetic.main.widget_input_center.view.*

class InputViewCenter : InputView {

    /**
     * [AppCustomView] override
     */
    override val textViewTitle: AppCompatTextView? get() = inputTextViewTitle

    override val textViewError: AppCompatTextView? get() = inputTextViewError

    override val editText: AppCompatEditText? get() = inputEditText

    override fun layoutResource(): Int {
        return R.layout.widget_input_center
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

}