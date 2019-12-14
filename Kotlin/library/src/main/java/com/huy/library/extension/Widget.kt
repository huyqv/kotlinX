package com.huy.library.extension

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.text.Html
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import java.util.*


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/02/24
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */


/**
 * @EditText
 */
fun EditText?.showKeyboard() {
    this?.apply {
        clearFocus()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}

/**
 * @ImageView
 */
fun ImageView.setTintColor(color: String) {
    post { this.setColorFilter(Color.parseColor(color)) }
}

fun ImageView.setTintColor(@ColorRes color: Int) {
    post { this.setColorFilter(color) }
}

fun ImageView.postImage(@DrawableRes drawableRes: Int) {
    post { this.setImageResource(drawableRes) }
}


/**
 * @NestedScrollView
 */
fun NestedScrollView.scrollToTop() {
    post {
        fling(0)
        scrollTo(0, 0)
    }
}

fun NestedScrollView.scrollToBottom(view: View) {
    post {
        fling(0)
        scrollTo(0, view.bottom)
    }
}

fun NestedScrollView.scrollToTop(view: View) {
    post {
        fling(0)
        scrollTo(0, view.top)
    }
}

fun NestedScrollView.scrollToCenter(view: View) {
    Handler().post {
        val top = view.top
        val bot = view.bottom
        val height = this.height
        this.scrollTo(0, (top + bot - height) / 2)
    }
}

fun HorizontalScrollView.scrollToCenter(view: View) {
    Handler().post {
        val left = view.left
        val right = view.right
        val width = this.width
        this.scrollTo((left + right - width) / 2, 0)
    }
}


/**
 * @RadioGroup
 */
fun RadioGroup.checkedButton(): View? {
    for (i in 0 until this.childCount) {
        val v = this.getChildAt(i)
        if (v is RadioButton && v.isChecked)
            return v
    }
    return null
}


/**
 * @TextView
 */
@SuppressLint("ResourceType")
fun TextView.draw(title: String, @ColorRes textColor: Int, @DrawableRes drawableRes: Int) {
    text = title
    draw(textColor, drawableRes)
}

@SuppressLint("ResourceType")
fun TextView.draw(@StringRes title: Int, @ColorRes textColor: Int, @DrawableRes drawableRes: Int) {
    setText(title)
    draw(textColor, drawableRes)
}

fun TextView.draw(@ColorRes colorRes: Int, @DrawableRes drawableRes: Int) {
    color(colorRes)
    post { setBackgroundResource(drawableRes) }
}

fun TextView.color(@ColorRes colorRes: Int) {
    setTextColor(ContextCompat.getColor(context, colorRes))
}

fun TextView.setHyperText(string: String) {

    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(string, 1)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(string)
    }
}

fun TextView.randomCapcha(level: Int = 0) {

    // Define max cap length
    val maxLength = 9
    // Define random value scope
    val scope = "qwertyuiopasdfghjklmnbvcxzQWERTYUIOPLKJHGFDSAZXCVBNM0123456789"

    val bonusCapCharacter = level * 2
    var capLength = 5 + bonusCapCharacter
    if (capLength > maxLength)
        capLength = maxLength

    val capText = CharArray(capLength)
    for (i in capText.indices)
        capText[i] = scope[Random().nextInt(scope.length)]

    this.text = capText.toString()
}

fun TextView.addEditorActionListener(actionId: Int, block: (String?) -> Unit) {
    maxLines = 1
    imeOptions = actionId
    setImeActionLabel(null, actionId)
    setOnEditorActionListener(object : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == actionId) {
                (context as? Activity)?.currentFocus?.windowToken.run {
                    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow((context as Activity).currentFocus?.windowToken, 0)
                }
                block(text.toString())
                return true
            }
            return false
        }
    })
}


/**
 * @WebView
 */
fun WebView.setupWebView() {
    settings.builtInZoomControls = true
    settings.displayZoomControls = true
    settings.javaScriptEnabled = true
    settings.defaultTextEncodingName = "utf-8"
}

fun WebView.setupTextView() {
    settings.builtInZoomControls = false
    settings.displayZoomControls = false
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
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.loadUrl(url)
            return super.shouldOverrideUrlLoading(view, url)
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
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





