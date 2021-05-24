package com.kotlin.app.ui.dialog.web

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kotlin.app.R
import com.kotlin.app.ui.main.MainDialog
import kotlinx.android.synthetic.main.web.*

class WebFragment : MainDialog() {

    /**
     * [MainDialog] implements
     */
    override fun layoutResource(): Int {
        return R.layout.web
    }

    override fun onViewCreated() {
        addClickListener(viewClose, textViewTitle)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    override fun onLiveDataObserve() {
        dialogVM.webLiveData.observe {
            textViewTitle.text = it.title
            loadUrlToWebView(it.url)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadUrlToWebView(url: String) {
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        webView.loadUrl(url)
    }

    override fun onViewClick(v: View?) {
        when (v) {
            viewClose -> dismiss()
            textViewTitle -> return
        }
    }

}

