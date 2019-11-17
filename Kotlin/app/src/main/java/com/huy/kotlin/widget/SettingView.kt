package com.huy.kotlin.lib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.huy.kotlin.R
import kotlinx.android.synthetic.main.widget_setting.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/11/4
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SettingView : ConstraintLayout {

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        val types = context.theme.obtainStyledAttributes(attrs, R.styleable.SettingView, 0, 0)

        try {

            val hint = types.getString(R.styleable.SettingView_android_hint)
            val text = types.getString(R.styleable.SettingView_android_text)
            val hintColor = types.getResourceId(R.styleable.SettingView_android_textColorHint, R.color.colorDark)
            val textColor = types.getResourceId(R.styleable.SettingView_android_textColor, R.color.colorGrey)
            val checkable = types.getBoolean(R.styleable.SettingView_android_checkable, false)
            val checked = types.getBoolean(R.styleable.SettingView_android_checked, false)
            val separator = types.getBoolean(R.styleable.SettingView_setting_separator, true)

            LayoutInflater.from(context).inflate(R.layout.widget_setting, this)
            setting_title.text = hint
            setting_title.setTextColor(ContextCompat.getColor(context, hintColor))

            setting_text.text = text
            setting_text.setTextColor(ContextCompat.getColor(context, textColor))

            setting_separator.visibility = if (separator) View.VISIBLE else View.INVISIBLE

            if (checkable) {

                setting_checkbox.visibility = View.VISIBLE
                setting_checkbox.isChecked = checked
                setting_view.setOnClickListener { isChecked = !isChecked }
            } else {

                setting_checkbox.visibility = View.INVISIBLE
            }

        } finally {
            types.recycle()
        }

    }

    var text: String
        get() = setting_text.text.toString()
        set(value) {
            setting_text.text = value
        }

    var isChecked: Boolean
        get() = setting_checkbox.isChecked
        set(value) {
            setting_checkbox.isChecked = value
        }

    var onCheckedChangedListener: CompoundButton.OnCheckedChangeListener?
        get() = onCheckedChangedListener
        set(value) {
            setting_checkbox.setOnCheckedChangeListener(value)
        }

}
