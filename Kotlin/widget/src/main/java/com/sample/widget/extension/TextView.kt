package com.sample.widget.extension

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.InputFilter
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.sample.widget.app
import java.util.*

class InputFilterBuilder {

    private val filters = mutableListOf<InputFilter>()

    fun maxLength(length: Int) {
        filters.add(InputFilter.LengthFilter(length))
    }

    fun allCaps() {
        filters.add(InputFilter.AllCaps())
    }

    fun charFilter(chars: CharArray) {
        filters.add(InputFilter { source, start, end, _, _, _ ->
            when {
                end > start -> for (index in start until end) {
                    if (!String(chars).contains(source[index].toString())) {
                        return@InputFilter ""
                    }
                }
            }
            return@InputFilter null
        })
    }

    fun regex(regex: String) {
        filters.add(InputFilter { source, start, end, _, _, _ ->
            when {
                end > start -> for (index in start until end) {
                    if (!source[index].toString().matches(regex.toRegex())) {
                        return@InputFilter ""
                    }
                }
            }
            return@InputFilter null
        })
    }

    fun build(): Array<out InputFilter> {
        return filters.toTypedArray()
    }
}

fun EditText?.buildFilter(block: InputFilterBuilder.() -> Unit) {
    this?.filters = InputFilterBuilder().also { it.block() }.build()
}

fun charsFilter(chars: CharArray): InputFilter {
    return InputFilter { source, start, end, _, _, _ ->
        when {
            end > start -> for (index in start until end) {
                if (!String(chars).contains(source[index].toString())) {
                    return@InputFilter ""
                }
            }
        }
        return@InputFilter null
    }
}

fun EditText.addFilter(filter: InputFilter) {
    val newFilter = mutableListOf<InputFilter>()
    newFilter.add(filter)
    if (!this.filters.isNullOrEmpty()) {
        newFilter.addAll(this.filters)
    }
    this.filters = newFilter.toTypedArray()
}

fun EditText.addCharsFilter(chars: CharArray) {
    addFilter(charsFilter(chars))
}

fun EditText?.addOnClickListener(listener: View.OnClickListener) {
    this ?: return
    isFocusable = false
    isCursorVisible = false
    keyListener = null
    inputType = EditorInfo.IME_ACTION_NONE
    setOnClickListener { listener.onClick(this) }
}

fun EditText?.showKeyboard() {
    this?.post {
        requestFocus()
        val imm: InputMethodManager? =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun EditText?.hideKeyboard() {
    this?.post {
        clearFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
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
                    //hideKeyboard()
                    //clearFocus()
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

fun EditText.disableFocus() {
    hideKeyboard()
    clearFocus()
    isFocusable = false
    isCursorVisible = false
}

fun EditText.enableFocus() {
    isFocusable = true
    isCursorVisible = true
}

/**
 *
 */
val TextView.trimText: String
    get() {
        val s = this.text?.toString().trimText
        text = s
        if (this is EditText && this.hasFocus()) {
            setSelection(s.length)
        }
        return s
    }

fun TextView.textColor(@ColorRes color: Int) {
    setTextColor(ContextCompat.getColor(context, color))
}

fun TextView.fontFamily(int: Int) {
    this.typeface = ResourcesCompat.getFont(app, int)
}

fun TextView.setHyperText(@StringRes res: Int, vararg args: Any?) {
    setHyperText(string(res), * args)
}

fun TextView.setHyperText(s: String?, vararg args: Any?) {
    post {
        text = try {
            when {
                s.isNullOrEmpty() -> null
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> Html.fromHtml(s.format(*args), Html.FROM_HTML_MODE_LEGACY)
                else -> {
                    @Suppress("DEPRECATION")
                    Html.fromHtml(s.format(*args))
                }
            }
        } catch (e: Throwable) {
            s
        }
    }
}

fun TextView.gradientHorizontal(@ColorRes colorStart: Int, @ColorRes colorEnd: Int = colorStart) {
    paint.shader = LinearGradient(
        0f, 0f, this.width.toFloat(), 0f,
        ContextCompat.getColor(context, colorStart),
        ContextCompat.getColor(context, colorEnd),
        Shader.TileMode.CLAMP
    )
}

fun TextView.gradientVertical(@ColorRes colorStart: Int, @ColorRes colorEnd: Int = colorStart) {
    paint.shader = LinearGradient(
        0f, 0f, 0f, this.height.toFloat(),
        ContextCompat.getColor(context, colorStart),
        ContextCompat.getColor(context, colorEnd),
        Shader.TileMode.CLAMP
    )
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
    typeface = Typeface.create(this.typeface, Typeface.NORMAL)
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