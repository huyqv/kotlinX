package com.sample.widget.extension

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Html
import android.text.InputFilter
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import com.sample.widget.app
import java.util.*

/**
 *
 */
fun ImageView.tint(@ColorInt color: Int) {
    post {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}

fun ImageView.tintRes(@ColorRes res: Int) {
    tint(ContextCompat.getColor(context, res))
}

fun ImageView.postImage(@DrawableRes drawableRes: Int) {
    post { this.setImageResource(drawableRes) }
}

/**
 *
 */
val NestedScrollView?.hasInvisibleScrollContent: Boolean
    @SuppressLint("RestrictedApi")
    get() {
        this ?: return false
        return this.scrollY < (this.computeVerticalScrollRange() - this.height)
    }

fun NestedScrollView.scrollToTop() {
    post {
        fling(0)
        smoothScrollTo(0, 0)
    }
}

fun NestedScrollView.scrollToBottom(view: View) {
    post {
        fling(0)
        smoothScrollTo(0, view.bottom)
    }
}

fun NestedScrollView.scrollToTop(view: View?) {
    view ?: return
    post {
        fling(0)
        smoothScrollTo(0, view.top)
    }
}

fun NestedScrollView.scrollToCenter(view: View) {
    post {
        val top = view.top
        val bot = view.bottom
        val height = this.height
        this.smoothScrollTo(0, (top + bot - height) / 2)
    }
}

fun HorizontalScrollView.scrollToCenter(view: View) {
    post {
        val left = view.left
        val right = view.right
        val width = this.width
        this.scrollTo((left + right - width) / 2, 0)
    }
}

/**
 *
 */
fun RadioGroup.checkedButton(): View? {
    for (i in 0 until this.childCount) {
        val v = this.getChildAt(i)
        if (v is RadioButton && v.isChecked)
            return v
    }
    return null
}

fun RadioGroup.addOnCheckedChangeListener(block: (RadioButton) -> Unit) {
    setOnCheckedChangeListener { _, checkedId ->
        val button = (context as Activity).findViewById<RadioButton>(checkedId)
        block(button)
    }
}



/**
 *
 */
fun WebView.setupWebView() {
    settings.builtInZoomControls = true
    settings.displayZoomControls = true
    settings.javaScriptEnabled = true
    settings.defaultTextEncodingName = "utf-8"
}

fun WebView.setChromeClient(progressBar: ProgressBar) {

    webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, progress: Int) {
            try {
                if (progress < 100 && progressBar.visibility == View.GONE) {
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = progress
                } else {
                    progressBar.visibility = View.GONE
                }
            } catch (ex: IllegalStateException) {
            }
        }
    }
}

fun WebView.setClient(progressBar: ProgressBar) {

    webViewClient = object : WebViewClient() {
        @Suppress("OverridingDeprecatedMember", "DEPRECATION")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
            view?.loadUrl(url)
            return super.shouldOverrideUrlLoading(view, url)
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            view?.loadUrl(request?.url.toString())
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            try {
                progressBar.visibility = View.VISIBLE
            } catch (ex: IllegalStateException) {
            }
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            try {
                progressBar.visibility = View.INVISIBLE
            } catch (ex: IllegalStateException) {
            }
            super.onPageFinished(view, url)
        }
    }
}

fun createDrawable(@ColorInt startColor: Int, endColor: Int, radius: Float): Drawable {
    val gradientDrawable =
        GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(startColor, endColor));
    gradientDrawable.cornerRadius = radius;
    return gradientDrawable
}








