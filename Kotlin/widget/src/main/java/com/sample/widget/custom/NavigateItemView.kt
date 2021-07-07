package com.sample.widget.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sample.widget.databinding.NavItemBinding

class NavigateItemView : AppCustomView<NavItemBinding> {

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun inflating(): (LayoutInflater, ViewGroup, Boolean) -> NavItemBinding {
        return NavItemBinding::inflate
    }

    override fun onInitialize(context: Context, types: TypedArray) {
        bind.textViewLabel.isClickable = false
        bind.textViewLabel.text = types.text
        bind.textViewLabel.isAllCaps = types.textAllCaps
        bind.textViewLabel.setTextColor(types.textColor)
        bind.imageViewIcon.setImageDrawable(types.src)
        bind.layoutContent.setBackgroundResource(types.background)
    }

}
