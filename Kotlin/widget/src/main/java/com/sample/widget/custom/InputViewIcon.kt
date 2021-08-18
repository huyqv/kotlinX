package com.sample.widget.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.sample.widget.databinding.InputIconBinding

class InputViewIcon : InputView<InputIconBinding> {

    override val textViewTitle: AppCompatTextView? get() = bind.textViewTitle

    override val textViewError: AppCompatTextView? get() = bind.textViewError

    override val editText: AppCompatEditText? get() = bind.editText

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun inflating(): (LayoutInflater, ViewGroup, Boolean) -> InputIconBinding {
        return InputIconBinding::inflate
    }

    override fun configDrawable(types: TypedArray) {
        bind.imageViewIcon.also {
            it.setColorFilter(types.tint)
            it.setImageDrawable(types.drawable)
        }
    }

}