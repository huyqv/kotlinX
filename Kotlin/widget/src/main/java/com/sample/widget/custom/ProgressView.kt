package com.sample.widget.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sample.widget.databinding.ProgressBinding

class ProgressView : AppCustomView<ProgressBinding> {

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun inflating(): (LayoutInflater, ViewGroup, Boolean) -> ProgressBinding {
        return ProgressBinding::inflate
    }

    override fun onInitialize(context: Context, types: TypedArray) {
    }

}