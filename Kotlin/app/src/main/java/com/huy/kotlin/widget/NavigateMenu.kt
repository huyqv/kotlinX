package com.huy.kotlin.lib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.huy.kotlin.R
import kotlinx.android.synthetic.main.widget_menu.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/11/4
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class NavigateMenu : ConstraintLayout {

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.NavigateMenu, 0, 0)

        try {

            val text = a.getString(R.styleable.NavigateMenu_android_text)
            val textColor = a.getInt(R.styleable.NavigateMenu_android_textColor, -1)
            val allCaps = a.getBoolean(R.styleable.NavigateMenu_android_textAllCaps, false)
            val drawable = a.getResourceId(R.styleable.NavigateMenu_android_src, -1)
            val background = a.getResourceId(R.styleable.NavigateMenu_android_background, -1)

            LayoutInflater.from(context).inflate(R.layout.widget_menu, this)
            viewMenu_textView.text = text
            viewMenu_textView.isClickable = false
            viewMenu_textView.isAllCaps = allCaps

            if (textColor != -1) viewMenu_textView.setTextColor(textColor)

            if (drawable != -1) viewMenu_icon.setBackgroundResource(drawable)

            if (background != -1) viewMenu_view.setBackgroundResource(background)

        } finally {
            a.recycle()
        }
    }

}
