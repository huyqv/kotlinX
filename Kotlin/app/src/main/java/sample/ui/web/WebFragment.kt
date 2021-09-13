package sample.ui.web

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kotlin.app.databinding.WebBinding
import sample.ui.main.MainDialogFragment

class WebFragment : MainDialogFragment<WebBinding>() {

    override fun inflating(): (LayoutInflater) -> WebBinding {
        return WebBinding::inflate
    }

    override fun onViewCreated() {
        addClickListener(bind.viewClose, bind.textViewTitle)
        bind.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    override fun onLiveDataObserve() {
        dialogVM.webLiveData.observe {
            if (null != it) {
                bind.textViewTitle.text = it.title
                bind.webView.load(it.url)
            } else {
                dismissAllowingStateLoss()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun WebView.load(url: String) {
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        loadUrl(url)
    }

    override fun onViewClick(v: View?) {
        when (v) {
            bind.viewClose -> dismiss()
            bind.textViewTitle -> return
        }
    }

}

