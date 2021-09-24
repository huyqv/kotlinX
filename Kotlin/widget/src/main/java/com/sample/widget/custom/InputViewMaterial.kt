package com.sample.widget.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.viewbinding.ViewBinding
import com.sample.widget.databinding.InputMaterialBinding

class InputViewMaterial : InputView<InputMaterialBinding> {

    override val textViewTitle: AppCompatTextView? get() = bind.textViewTitle

    override val textViewError: AppCompatTextView? get() = bind.textViewError

    override val editText: AppCompatEditText? get() = bind.editText

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun inflating(): (LayoutInflater, ViewGroup, Boolean) -> ViewBinding {
        return InputMaterialBinding::inflate
    }
}