package com.huy.kotlin.widget

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.text.InputFilter
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.huy.kotlin.R

abstract class CustomInputView : CustomView {

    var title: String?
        get() = titleView.text.toString()
        set(value) {
            titleView.text = value
        }

    var error: String?
        get() = errorView.text.toString()
        set(value) {
            errorView.text = value
        }

    var text: String?
        get() = editView.text.toString()
        set(value) {
            editView.setText(value)
        }

    val notError: Boolean get() = error.isNullOrEmpty()

    val hasError: Boolean get() = !notError

    val isEmpty: Boolean get() = text.isNullOrEmpty()

    val notEmpty: Boolean get() = !isEmpty

    abstract val titleView: TextView

    abstract val errorView: TextView

    abstract val editView: EditText

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onInitialize(context: Context, types: TypedArray) {
        clearBackground()

        configTitleText(types)

        configEditText(types)

        configDrawable(types)
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        editView.apply {
            isFocusable = false
            isCursorVisible = false
            keyListener = null
            inputType = EditorInfo.IME_ACTION_NONE
        }
        super.setOnClickListener {
            listener?.onClick(this@CustomInputView)
        }
    }

    fun setError(@StringRes res: Int?) {
        if (res == null) errorView.text = null
        else errorView.setText(res)
    }

    fun clear() {
        editView.text = null
        errorView.text = null
    }

    fun addEditorActionListener(actionId: Int, block: (String?) -> Unit) {
        editView.apply {
            maxLines = 1
            imeOptions = actionId
            setImeActionLabel(null, actionId)
            setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                    if (actionId == actionId) {
                        hideKeyboard()
                        block(this@CustomInputView.text)
                        return true
                    }
                    return false
                }
            })
        }
    }

    fun setDrawableRight(@DrawableRes drawableRes: Int) {
        editView.post {
            val drawable = ContextCompat.getDrawable(context, drawableRes)
            editView.setCompoundDrawables(null, null, drawable, null)
        }
    }

    fun setDrawableClickListener(block: () -> Unit) {
        editView.apply {
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
        val color = getTypedColor(types, R.styleable.InputView_android_textColor)
        titleView.setTextColor(color)
        titleView.text = types.getString(R.styleable.InputView_android_text)
    }

    open fun configEditText(types: TypedArray) {

        val inputType = types.getInt(R.styleable.InputView_android_inputType, EditorInfo.TYPE_CLASS_TEXT)
        if (inputType == EditorInfo.TYPE_NULL) {
            setOnKeyListener(null)
            editView.isFocusable = false
            editView.isEnabled = false
            editView.isCursorVisible = false
        } else {
            editView.inputType = inputType
        }

        editView.maxLines = types.getInt(R.styleable.InputView_android_maxLines, 1)

        val filters = arrayListOf<InputFilter>()

        val allCaps = types.getBoolean(R.styleable.InputView_android_textAllCaps, false)
        if (allCaps) filters.add(InputFilter.AllCaps())

        val maxLength = types.getInt(R.styleable.InputView_android_maxLength, 100)
        filters.add(InputFilter.LengthFilter(maxLength))

        val array = arrayOfNulls<InputFilter>(filters.size)
        editView.filters = filters.toArray(array)

        val nextFocus = types.getResourceId(R.styleable.InputView_android_nextFocusForward, -1)
        if (nextFocus != -1) editView.nextFocusForwardId = nextFocus

        val imeOption = types.getInt(R.styleable.InputView_android_imeOptions, -1)
        if (imeOption != -1) editView.imeOptions = imeOption

        val src = types.getResourceId(R.styleable.InputView_android_src, -1)
        if (src != -1) editView.setBackgroundResource(src)

    }

    open fun configDrawable(types: TypedArray) {

        val color = getTypedColor(types, R.styleable.InputView_android_textColor)

        val drawableLeft = getTypedDrawable(types, R.styleable.InputView_android_drawableStart, color)

        val drawableRight = getTypedDrawable(types, R.styleable.InputView_android_drawableEnd, color)

        editView.post {
            editView.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null)
        }
    }

    private fun clearBackground() {
        isClickable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) this.background = null
        else this.setBackgroundResource(0)
    }

    private fun hideKeyboard() {
        (context as? Activity)?.currentFocus?.windowToken.run {
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow((context as Activity).currentFocus?.windowToken, 0)
        }
    }

}