package com.kotlin.app.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.example.library.widget.AppCustomView
import com.kotlin.app.R
import kotlinx.android.synthetic.main.widget_input_icon.view.*

class InputViewIcon : InputView {

    /**
     * [AppCustomView] override
     */
    override val textViewTitle: AppCompatTextView? get() = inputTextViewTitle

    override val textViewError: AppCompatTextView? get() = inputTextViewError

    override val editText: AppCompatEditText? get() = inputEditText

    override fun layoutResource(): Int {
        return R.layout.widget_input_icon
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun configDrawable(types: TypedArray) {
        inputImageViewDrawable.also {
            it.setColorFilter(types.drawableTint)
            it.setImageDrawable(types.drawable)
        }
    }

}