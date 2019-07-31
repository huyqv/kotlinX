package com.huy.library.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView


/**
 * --------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/21
 * @Description: ...
 * All Right Reserved
 * --------------------------------------------------------------------------------
 */
class HyperTextView : WebView, View.OnTouchListener {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        setBackgroundColor(Color.TRANSPARENT)
        setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        setOnTouchListener(null)
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }

    fun loadHtmlString(htmlString: String?) {
        if (!htmlString.isNullOrEmpty())
            this.loadData(htmlString, "text/html", "UTF-8")
    }

}