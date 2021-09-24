package com.sample.widget.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.sample.widget.databinding.ProgressBinding

class ProgressBar : AppCustomView<ProgressBinding> {

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun inflating(): (LayoutInflater, ViewGroup, Boolean) -> ViewBinding {
        return ProgressBinding::inflate
    }

    override fun onInitialize(context: Context, types: TypedArray) {
    }
}