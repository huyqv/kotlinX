package com.kotlin.app.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
import com.kotlin.app.R
import com.example.library.widget.AppCustomView
import kotlinx.android.synthetic.main.widget_setting.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/11/4
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SettingView : AppCustomView {

    /**
     * [AppCustomView] override
     */
    override fun layoutResource(): Int {
        return R.layout.widget_setting
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun onInitialize(context: Context, types: TypedArray) {

        settingViewContent.setBackgroundResource(types.background)
        settingViewContent.setPadding(types.paddingStart, types.paddingTop, types.paddingEnd, types.paddingBottom)
        settingTextViewTitle.text = types.title
        settingTextViewTitle.setTextColor(types.hintColor)

        settingTextViewProperty.text = types.text
        settingTextViewProperty.setTextColor(types.textColor)

        if (types.checkable) {
            settingCheckbox.visibility = View.VISIBLE
            settingCheckbox.isChecked = types.checked
            settingViewContent.setOnClickListener { isChecked = !isChecked }
        } else {
            settingCheckbox.visibility = View.INVISIBLE
        }
    }

    /**
     * [SettingView] properties
     */
    var text: String?
        get() = settingTextViewProperty.text?.toString()
        set(value) {
            settingTextViewProperty.text = value
        }

    var isChecked: Boolean
        get() = settingCheckbox.isChecked
        set(value) {
            settingCheckbox.isChecked = value
        }

    var onCheckedChangedListener: CompoundButton.OnCheckedChangeListener?
        get() = null
        set(value) {
            settingCheckbox.setOnCheckedChangeListener(value)
        }


}
