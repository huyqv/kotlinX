package com.sample.widget.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
import android.os.Build
import android.text.InputFilter
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.sample.widget.R
import com.sample.widget.extension.addViewClickListener

abstract class InputView<B : ViewBinding> : AppCustomView<B>, View.OnFocusChangeListener {

    /**
     * [AppCustomView] override
     */
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun onInitialize(context: Context, types: TypedArray) {
        clearBackground()
        configTitleText(types)
        configEditText(types)
        configDrawable(types)

    }

    override fun setOnClickListener(listener: OnClickListener?) {
        if (null == listener) {
            enableFocus()
            editText.addViewClickListener(null)
        } else {
            disableFocus()
            editText.addViewClickListener {
                listener.onClick(this)
            }
        }
    }

    /**
     * Focus properties
     */
    private val onFocusChange = mutableListOf<(Boolean) -> Unit>()

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        onFocusChange.forEach { it(hasFocus) }
    }

    override fun hasFocusable(): Boolean {
        return false
    }

    override fun isFocused(): Boolean {
        return false
    }

    override fun hasFocus(): Boolean {
        return false
    }

    override fun clearFocus() {
        editText?.clearFocus()
        hideKeyboard()
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        editText?.also {
            if (!it.isFocused) it.post {
                it.requestFocus()
                showKeyboard()
            }
        }
        return false
    }

    fun addOnFocusChangeListener(block: (Boolean) -> Unit) {
        onFocusChange.add(block)
    }

    fun disableFocus() {
        editText?.apply {
            isFocusable = false
            isCursorVisible = false
        }
    }

    fun enableFocus() {
        editText?.apply {
            isFocusable = true
            isCursorVisible = true
        }
    }


    /**
     * [InputView] properties
     */
    var title: String?
        get() = textViewTitle?.text.toString()
        set(value) {
            textViewTitle?.text = value
        }

    var error: String?
        get() = textViewError?.text.toString()
        set(value) {
            textViewError?.text = value
        }

    var text: String?
        get() = editText?.text.toString()
        set(value) {
            editText?.setText(value)
        }

    val notError: Boolean get() = error.isNullOrEmpty()

    val hasError: Boolean get() = !notError

    val isEmpty: Boolean get() = text.isNullOrEmpty()

    val notEmpty: Boolean get() = !isEmpty

    abstract val textViewTitle: AppCompatTextView?

    abstract val textViewError: AppCompatTextView?

    abstract val editText: AppCompatEditText?

    fun setError(@StringRes res: Int?) {
        if (res == null) textViewError?.text = null
        else textViewError?.setText(res)
    }

    fun clear() {
        editText?.text = null
        textViewError?.text = null
    }

    fun addEditorActionListener(actionId: Int, block: (String?) -> Unit) {
        editText?.apply {
            maxLines = 1
            imeOptions = actionId
            setImeActionLabel(null, actionId)
            setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                    if (actionId == actionId) {
                        hideKeyboard()
                        block(this@InputView.text)
                        return true
                    }
                    return false
                }
            })
        }
    }

    fun setDrawableRight(@DrawableRes drawableRes: Int) {
        editText?.post {
            val drawable = ContextCompat.getDrawable(context, drawableRes)
            editText?.setCompoundDrawables(null, null, drawable, null)
        }
    }

    fun setDrawableClickListener(block: () -> Unit) {
        editText?.apply {
            setOnTouchListener(OnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP)
                // LEFT = 0, TOP = 1, RIGHT = 2, BOTTOM = 3
                    if (event.rawX >= right - compoundDrawables[2].bounds.width()) {
                        block()
                        return@OnTouchListener true
                    }
                false
            })
        }
    }

    open fun configTitleText(types: TypedArray) {
        textViewTitle?.setTextColor(types.tint)
        textViewTitle?.text = types.text
    }

    open fun configEditText(types: TypedArray) {

        editText?.also {

            it.onFocusChangeListener = this

            it.setTextColor(types.textColor)

            val inputType = types.getInt(R.styleable.AppCustomView_android_inputType, EditorInfo.TYPE_CLASS_TEXT)
            if (inputType == EditorInfo.TYPE_NULL) {
                setOnKeyListener(null)
                it.isFocusable = false
                it.isEnabled = false
                it.isCursorVisible = false
            } else {
                it.inputType = inputType
            }

            it.maxLines = types.getInt(R.styleable.AppCustomView_android_maxLines, 1)

            val filters = arrayListOf<InputFilter>().apply {
                add(InputFilter.LengthFilter(types.maxLength))
                if (types.textAllCaps) add(InputFilter.AllCaps())
            }

            it.filters = filters.toArray(arrayOfNulls<InputFilter>(filters.size))

            it.nextFocusForwardId = types.getResourceId(R.styleable.AppCustomView_android_nextFocusForward, -1)

            it.imeOptions = types.getInt(R.styleable.AppCustomView_android_imeOptions, EditorInfo.IME_ACTION_NEXT)

            val src = types.getResourceId(R.styleable.AppCustomView_android_src, -1)
            if (src != -1) editText?.setBackgroundResource(src)

        }
    }

    open fun configDrawable(types: TypedArray) {
        val color = types.drawableTint
        val drawableLeft = types.drawableStart.tint(color)
        val drawableRight = types.drawableEnd.tint(color)
        editText?.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null)
    }

    private fun clearBackground() {
        isClickable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) this.background = null
        else this.setBackgroundResource(0)
    }

    fun showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(editText?.windowToken, 0)
    }


}