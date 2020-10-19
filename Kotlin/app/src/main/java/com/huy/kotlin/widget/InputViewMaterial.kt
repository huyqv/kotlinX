package com.huy.kotlin.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.huy.kotlin.R
import com.huy.library.widget.AppCustomView
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
    override val titleView: AppCompatTextView? get() = inputTextViewTitle

    override val errorView: AppCompatTextView? get() = inputTextViewError

    override val editView: AppCompatEditText? get() = inputEditText

    override fun layoutResource(): Int {
        return R.layout.widget_input_material
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

}