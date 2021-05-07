package com.example.library.extension

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.InputFilter
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
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
import com.example.library.Library
import java.util.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/02/24
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun EditText.filterChars(chars: CharArray) {
    val arrayList = arrayListOf<InputFilter>()
    this.filters?.apply { arrayList.addAll(this) }
    arrayList.add(InputFilter { source, start, end, _, _, _ ->
        when {
            end > start -> for (index in start until end) {
                if (!String(chars).contains(source[index].toString())) {
                    return@InputFilter ""
                }
            }
        }
        return@InputFilter null
    })
    this.filters = arrayList.toArray(arrayOfNulls<InputFilter>(arrayList.size))
    this.inputType = EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS
}

fun EditText?.addOnClickListener(listener: View.OnClickListener) {
    this ?: return
    isFocusable = false
    isCursorVisible = false
    keyListener = null
    inputType = EditorInfo.IME_ACTION_NONE
    setOnClickListener { listener.onClick(this) }
}

/**
 * Add editor action & modify soft keyboard action
 * @actionId see [android.view.inputmethod.EditorInfo]
 */
fun EditText.addEditorActionListener(actionId: Int, block: (String?) -> Unit) {
    imeOptions = actionId
    setImeActionLabel(null, actionId)
    setOnEditorActionListener(object : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            if (imeOptions == actionId) {
                this@addEditorActionListener.post {
                    isSelected = false
                    block(text.toString())
                    (context as? Activity)?.hideKeyboard()
                    clearFocus()
                }
                return true
            }
            return false
        }
    })
}

fun EditText.addActionNextListener(block: (String?) -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_NEXT
    isSingleLine = true
    setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT)
    setOnEditorActionListener(object : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            if (imeOptions == actionId) {
                this@addActionNextListener.post {
                    block(text.toString())
                }
                return true
            }
            return false
        }
    })
}

fun EditText.addActionDoneListener(block: (String?) -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_DONE
    isSingleLine = true
    setImeActionLabel("Next", EditorInfo.IME_ACTION_DONE)
    setOnEditorActionListener(object : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            if (imeOptions == actionId) {
                this@addActionDoneListener.post {
                    isSelected = false
                    block(text.toString())
                    hideKeyboard()
                    clearFocus()
                }
                return true
            }
            return false
        }
    })
}

fun EditText.select() {
    this.setSelection(text?.toString()?.length ?: 0)
}

/**
 *
 */
fun ImageView.tintColor(@ColorInt color: Int) {
    post {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}

fun ImageView.postImage(@DrawableRes drawableRes: Int) {
    post { this.setImageResource(drawableRes) }
}

/**
 *
 */
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
val TextView?.trimText: String
    get() {
        this ?: return ""
        var s = text?.toString()
        if (s.isNullOrEmpty()) return ""
        s = s.replace("\\s+", " ").trimIndent()
        text = s
        if (this is EditText) {
            setSelection(s.length)
        }
        return s
    }

fun TextView.textColor(@ColorRes color: Int) {
    setTextColor(ContextCompat.getColor(context, color))
}

fun TextView.fontFamily(int: Int) {
    this.typeface = ResourcesCompat.getFont(Library.app, int)
}

fun TextView.setHyperText(@StringRes res: Int, vararg args: Any?) {
    setHyperText(string(res), * args)
}

fun TextView.setHyperText(s: String?, vararg args: Any?) {
    post {
        text = when {
            s.isNullOrEmpty() -> {
                null
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                Html.fromHtml(s.format(*args), Html.FROM_HTML_MODE_LEGACY  /*Html.FROM_HTML_MODE_COMPACT*/)
            }
            else -> {
                @Suppress("DEPRECATION")
                Html.fromHtml(s.format(*args))
            }
        }
    }
}

fun TextView.gradientHorizontal(@ColorRes colorStart: Int, @ColorRes colorEnd: Int = colorStart) {
    paint.shader = LinearGradient(0f, 0f, this.width.toFloat(), 0f,
            ContextCompat.getColor(context, colorStart),
            ContextCompat.getColor(context, colorEnd),
            Shader.TileMode.CLAMP)
}

fun TextView.gradientVertical(@ColorRes colorStart: Int, @ColorRes colorEnd: Int = colorStart) {
    paint.shader = LinearGradient(0f, 0f, 0f, this.height.toFloat(),
            ContextCompat.getColor(context, colorStart),
            ContextCompat.getColor(context, colorEnd),
            Shader.TileMode.CLAMP)
}

fun TextView.drawableStart(drawable: Drawable?) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
}

fun TextView.drawableEnd(drawable: Drawable?) {
    this.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
}

fun TextView.drawableTop(drawable: Drawable?) {
    this.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
}

fun TextView.drawableBottom(drawable: Drawable?) {
    this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable)
}

fun TextView.bold() {
    setTypeface(this.typeface, Typeface.BOLD)
}

fun TextView.regular() {
    setTypeface(this.typeface, Typeface.NORMAL)
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








