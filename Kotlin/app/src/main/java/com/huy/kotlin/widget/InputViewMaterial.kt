package com.huy.kotlin.lib.widget

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.os.Build
import android.text.InputFilter
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.huy.kotlin.R
import kotlinx.android.synthetic.main.widget_input_material.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class InputViewMaterial : FrameLayout {

    var title: String?
        get() = inputTextViewTitle.text.toString()
        set(value) {
            inputTextViewTitle.text = value
        }

    var text: String?
        get() = inputEditText.text.toString()
        set(value) {
            inputEditText.setText(value)
        }

    var error: String?
        get() = inputTextViewError.text.toString()
        set(value) {
            inputTextViewError.text = value
        }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        val types = context.theme.obtainStyledAttributes(attrs, R.styleable.InputView, 0, 0)

        try {

            LayoutInflater.from(context).inflate(R.layout.widget_input_material, this)
            isClickable = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) this.background = null
            else this.setBackgroundResource(0)

            configTitle(types)

            configEditText(types)

            configDrawable(types)

        } finally {
            types.recycle()
        }
    }

    private fun configTitle(types: TypedArray) {

        val textColor = types.getInt(R.styleable.InputView_android_textColor, ContextCompat.getColor(context, R.color.colorPrimary))
        inputTextViewTitle.text = types.getString(R.styleable.InputView_android_text)
        inputTextViewTitle.setTextColor(textColor)
    }

    private fun configEditText(types: TypedArray) {

        val inputType = types.getInt(R.styleable.InputView_android_inputType, EditorInfo.TYPE_CLASS_TEXT)
        if (inputType == EditorInfo.TYPE_NULL) {
            setOnKeyListener(null)
            inputEditText.isFocusable = false
            inputEditText.isEnabled = false
            inputEditText.isCursorVisible = false
        } else {
            inputEditText.inputType = inputType
        }


        inputEditText.maxLines = types.getInt(R.styleable.InputView_android_maxLines, 1)

        val filters = arrayListOf<InputFilter>()

        val allCaps = types.getBoolean(R.styleable.InputView_android_textAllCaps, false)
        if (allCaps) filters.add(InputFilter.AllCaps())

        val maxLength = types.getInt(R.styleable.InputView_android_maxLength, 100)
        filters.add(InputFilter.LengthFilter(maxLength))

        val array = arrayOfNulls<InputFilter>(filters.size)
        inputEditText.filters = filters.toArray(array)

        val nextFocus = types.getResourceId(R.styleable.InputView_android_nextFocusForward, -1)
        if (nextFocus != -1) inputEditText.nextFocusForwardId = nextFocus

        val imeOption = types.getInt(R.styleable.InputView_android_imeOptions, -1)
        if (imeOption != -1) inputEditText.imeOptions = imeOption

        val src = types.getResourceId(R.styleable.InputView_android_src, -1)
        if (src != -1) inputEditText.setBackgroundResource(src)

    }

    private fun configDrawable(types: TypedArray) {

        val resStart = types.getResourceId(R.styleable.InputView_android_drawableStart, 0)
        val drawableLeft = if (resStart == 0) null else ContextCompat.getDrawable(context, resStart)
        val resEnd = types.getResourceId(R.styleable.InputView_android_drawableEnd, 0)
        val drawableRight = if (resEnd == 0) null else ContextCompat.getDrawable(context, resEnd)

        val color = types.getInt(R.styleable.InputView_android_textColor, ContextCompat.getColor(context, R.color.colorPrimary))
        if (color != 0) {
            if (null != drawableLeft) {
                DrawableCompat.setTint(drawableLeft, color)
                DrawableCompat.setTintMode(drawableLeft, PorterDuff.Mode.SRC_IN)
            }
            if (null != drawableRight) {
                DrawableCompat.setTint(drawableRight, color)
                DrawableCompat.setTintMode(drawableRight, PorterDuff.Mode.SRC_IN)
            }
        }
        inputEditText.apply {
            post {
                setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null)
            }
        }
    }

    override fun setOnClickListener(listener: View.OnClickListener) {

        inputEditText.apply {
            isFocusable = false
            isCursorVisible = false
            keyListener = null
            inputType = EditorInfo.IME_ACTION_NONE
            setOnClickListener { listener.onClick(this@InputViewMaterial) }
        }
    }

    fun setError(@StringRes stringRes: Int?) {
        if (stringRes == null) inputTextViewError.text = null
        else inputTextViewError.setText(stringRes)
    }

    fun setTextCaps(s: String?) {
        text = s?.toUpperCase()
    }

    fun setDrawableRight(@DrawableRes drawableRes: Int) {

        inputEditText.post {
            val drawable = ContextCompat.getDrawable(context, drawableRes)
            inputEditText.setCompoundDrawables(null, null, drawable, null)
        }
    }

    fun setDrawableClickListener() {

        // LEFT = 0, TOP = 1, RIGHT = 2, BOTTOM = 3
        inputEditText.apply {
            setOnTouchListener(OnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP)
                    if (event.rawX >= right - compoundDrawables[2].bounds.width()) {
                        // handle click goes here
                        return@OnTouchListener true
                    }
                false
            })
        }
    }

    fun addEditorActionListener(actionId: Int, block: (String?) -> Unit) {
        inputEditText.apply {
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
    }

    fun clear() {
        inputEditText.text = null
        inputTextViewError.text = null
    }

    fun hasError(): Boolean {
        return !error.isNullOrEmpty()
    }

    fun notError(): Boolean {
        return error.isNullOrEmpty()
    }

    fun isEmpty(): Boolean {
        return text.isNullOrEmpty()
    }

    fun notEmpty(): Boolean {
        return !text.isNullOrEmpty()
    }

}