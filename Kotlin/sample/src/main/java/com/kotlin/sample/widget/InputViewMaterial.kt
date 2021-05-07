package com.kotlin.sample.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.kotlin.sample.R
import com.example.library.widget.AppCustomView
import kotlinx.android.synthetic.main.widget_input_material.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class InputViewMaterial : InputView {

    /**
     * [AppCustomView] override
     */
    override val textViewTitle: AppCompatTextView? get() = inputTextViewTitle

    override val textViewError: AppCompatTextView? get() = inputTextViewError

    override val editText: AppCompatEditText? get() = inputEditText

    override fun layoutResource(): Int {
        return R.layout.widget_input_material
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

}