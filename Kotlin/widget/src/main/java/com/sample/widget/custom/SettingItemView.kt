package com.sample.widget.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.sample.widget.databinding.SettingItemBinding

class SettingItemView : AppCustomView<SettingItemBinding> {

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun inflating(): (LayoutInflater, ViewGroup, Boolean) -> SettingItemBinding {
        return SettingItemBinding::inflate
    }

    override fun onInitialize(context: Context, types: TypedArray) {

        bind.layoutContent.setBackgroundResource(types.background)
        bind.layoutContent.setPadding(
                types.paddingStart,
                types.paddingTop,
                types.paddingEnd,
                types.paddingBottom
        )
        bind.textViewTitle.text = types.title
        bind.textViewTitle.setTextColor(types.hintColor)

        bind.textViewProperty.text = types.text
        bind.textViewProperty.setTextColor(types.textColor)

        if (types.checkable) {
            bind.checkbox.visibility = View.VISIBLE
            bind.checkbox.isChecked = types.checked
            bind.layoutContent.setOnClickListener { isChecked = !isChecked }
        } else {
            bind.checkbox.visibility = View.INVISIBLE
        }
    }

    /**
     * [SettingItemView] properties
     */
    var text: String?
        get() = bind.textViewProperty.text.toString()
        set(value) {
            bind.textViewProperty.text = value
        }

    var isChecked: Boolean
        get() = bind.checkbox.isChecked
        set(value) {
            bind.checkbox.isChecked = value
        }

    var onCheckedChangedListener: CompoundButton.OnCheckedChangeListener?
        get() = null
        set(value) {
            bind.checkbox.setOnCheckedChangeListener(value)
        }
}