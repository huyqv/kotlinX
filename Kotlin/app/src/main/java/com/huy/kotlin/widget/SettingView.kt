package com.huy.kotlin.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
import com.huy.kotlin.R
import com.huy.library.widget.AppCustomView
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
     * [AppCustomView] implement
     */
    override val layoutRes: Int get() = R.layout.widget_setting

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onInitialize(context: Context, types: TypedArray) {

        settingTextViewTitle.text = types.title
        settingTextViewTitle.setTextColor(types.hintColor)

        settingTextViewLabel.text = types.text
        settingTextViewLabel.setTextColor(types.textColor)

        if (types.checkable) {
            settingCheckbox.visibility = View.VISIBLE
            settingCheckbox.isChecked = types.checked
            settingViewContent.setOnClickListener { isChecked = !isChecked }
        } else {

            settingCheckbox.visibility = View.INVISIBLE
        }

        val separator = types.getBoolean(R.styleable.SettingView_setting_separator, true)
        settingViewSeparator.visibility = if (separator) View.VISIBLE else View.INVISIBLE
    }

    /**
     * [SettingView] properties
     */
    var text: String?
        get() = settingTextViewLabel.text?.toString()
        set(value) {
            settingTextViewLabel.text = value
        }

    var isChecked: Boolean
        get() = settingCheckbox.isChecked
        set(value) {
            settingCheckbox.isChecked = value
        }

    var onCheckedChangedListener: CompoundButton.OnCheckedChangeListener?
        get() = onCheckedChangedListener
        set(value) {
            settingCheckbox.setOnCheckedChangeListener(value)
        }


}
