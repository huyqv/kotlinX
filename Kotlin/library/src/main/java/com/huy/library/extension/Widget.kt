package com.huy.library.extension

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Handler
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
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
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
fun EditText?.showKeyboard() {
    this?.apply {
        clearFocus()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun EditText?.addOnClickListener(listener: View.OnClickListener) {
    this ?: return
    isFocusable = false
    isCursorVisible = false
    keyListener = null
    inputType = EditorInfo.IME_ACTION_NONE
    setOnClickListener { listener.onClick(this) }
}

fun ImageView.tintColor(color: String) {
    tintColor(Color.parseColor(color))
}

fun ImageView.tintColorRes(@ColorRes color: Int) {
    tintColor(color(color))
}

fun ImageView.tintColor(color: Int) {
    post {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            background.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}

fun ImageView.postImage(@DrawableRes drawableRes: Int) {
    post { this.setImageResource(drawableRes) }
}

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

fun TextView.color(@ColorRes color: Int) {
    setTextColor(ContextCompat.getColor(context, color))
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

/**
 * Add editor action & modify soft keyboard action
 * @actionId see [android.view.inputmethod.EditorInfo]
 */
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
 * Filter with a collection of allowed characters
 * @param: charArrayOf( 'A', 'b', 'c' ,'1')
 */
fun TextView.filter(filterChars: CharArray) {
    val arrayList = arrayListOf<InputFilter>()
    filters?.apply { arrayList.addAll(this) }
    arrayList.add(InputFilter { source, start, end, _, _, _ ->
        if (end > start) {
            for (index in start until end) {
                if (!String(filterChars).contains(source[index].toString())) {
                    return@InputFilter ""
                }
            }
        }
        null
    })
    filters = arrayList.toArray(arrayOfNulls<InputFilter>(arrayList.size))
}

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





