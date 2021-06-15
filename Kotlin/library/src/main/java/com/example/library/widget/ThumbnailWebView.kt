package com.example.library.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class ThumbnailWebView : WebView, View.OnTouchListener {

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    init {
        webViewClient = ThumbnailClient()
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        setOnTouchListener(this)
        val settings = settings
        settings.javaScriptEnabled = false
        settings.domStorageEnabled = true
    }

    override fun onTouch(p0: View?, p1: MotionEvent?) = false

    fun loadHtmlString(htmlString: String?) {
        if (!htmlString.isNullOrEmpty()) this.loadData(htmlString, "text/html", "UTF-8")
    }

    class ThumbnailClient : WebViewClient() {

        @Suppress("OverridingDeprecatedMember")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
            view?.loadUrl(url)
            return false
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            view?.loadUrl(request?.url.toString())
            return false
        }

    }

}