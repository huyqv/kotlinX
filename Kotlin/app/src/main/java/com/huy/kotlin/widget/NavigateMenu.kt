package com.huy.kotlin.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.huy.kotlin.R
import com.huy.library.widget.AppCustomView
import kotlinx.android.synthetic.main.widget_menu.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/11/4
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class NavigateMenu : AppCustomView {

    /**
     * [AppCustomView] override
     */
    override fun layoutResource(): Int {
        return R.layout.widget_menu
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun onInitialize(context: Context, types: TypedArray) {
        menuTextViewLabel.isClickable = false
        menuTextViewLabel.text = types.text
        menuTextViewLabel.isAllCaps = types.textAllCaps
        menuTextViewLabel.setTextColor(types.textColor)
        menuImageViewIcon.setImageDrawable(types.src)
        menuViewContent.background = types.backgroundDrawable
    }

}
