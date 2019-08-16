package com.huy.keyboard

import android.content.res.Resources
import android.content.res.TypedArray
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.annotation.StringRes

interface InputInterface : TextWatcher, KeyboardOutput {

    var title: String?
        get() = titleTextView?.text.toString()
        set(value) {
            titleTextView?.text = value
        }

    var text: String?
        get() = editText.text.toString()
        set(value) {
            editText.setText(value)
        }

    var error: String?
        get() = errorTextView?.text.toString()
        set(value) {
            if (value.isNullOrEmpty()) animateHideError()
            else animateShowError(value)
        }

    var hint: String?
        get() = editText.hint.toString()
        set(value) {
            editText.hint = value
        }

    val hasError: Boolean get() = !error.isNullOrEmpty()

    val notError: Boolean get() = !hasError

    val isEmpty: Boolean get() = text.isNullOrEmpty()

    val notEmpty: Boolean get() = !isEmpty

    fun setError(@StringRes res: Int?) {
        if (null == res) {
            error = null
        } else try {
            error = editText.context.getString(res)
        } catch (e: Resources.NotFoundException) {
            error = "Resources.NotFoundException"
        }
    }

    val titleTextView: TextView?

    val errorTextView: TextView?

    /**
     * Attribute
     */
    fun applyAttributeProperties(typedArray: TypedArray? = null) {


        // config title text
        titleTextView?.text = typedArray?.getString(R.styleable.InputView_android_title)

        // config edit text
        editText.apply {

            setTextIsSelectable(false)
            if (null == typedArray) {
                this.maxLines = 1
                this.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(256))
                return
            }

            customInputType = typedArray.getInt(R.styleable.InputView_android_inputType, EditorInfo.TYPE_CLASS_TEXT)
            if (customInputType == EditorInfo.TYPE_NULL) disableEditText()
            else inputType =
                    customInputType or EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS or EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING
            maxLines = typedArray.getInt(R.styleable.InputView_android_maxLines, 1)
            setText(typedArray.getString(R.styleable.InputView_android_text))
            hint = typedArray.getString(R.styleable.InputView_android_hint)
            val sFilters = arrayListOf<InputFilter>()
            textAllCaps = typedArray.getBoolean(R.styleable.InputView_android_textAllCaps, false)
            if (textAllCaps) sFilters.add(InputFilter.AllCaps())
            val sMaxLength = typedArray.getInt(R.styleable.InputView_android_maxLength, 256)
            sFilters.add(InputFilter.LengthFilter(sMaxLength))
            val array = arrayOfNulls<InputFilter>(sFilters.size)
            this.filters = sFilters.toArray(array)
            val imeOption = typedArray.getInt(R.styleable.InputView_android_imeOptions, -1)
            if (imeOption != -1) this.imeOptions = imeOption
        }

    }

    fun applyCustomInputMethod() {
        editText.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) showSoftInputOnFocus = false
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    CustomKeyboard.showKeyboard(this@InputInterface)
                }
            }
            setOnTouchListener { _, _ ->
                if (!hasOnClickListeners()) {
                    true
                }
                if (hasFocus()) {
                    if (CustomKeyboard.isProcess) return@setOnTouchListener true
                    CustomKeyboard.showKeyboard(this@InputInterface)
                    false

                }
                false
            }
        }
    }

    /**
     * Regex
     */
    fun addRegex(regex: String) {
        editText.filters = editText.filters.plus(
                listOf(InputFilter { source, _, _, _, _, _ ->
                    // for backspace
                    if (source == "") return@InputFilter source
                    if (!source.toString().matches(regex.toRegex())) return@InputFilter ""
                    return@InputFilter source
                })
        )
    }

    /**
     * Editor Action Listener
     */
    fun addEditorActionListener(@StringRes label: Int, block: () -> Unit) {
        addEditorActionListener(editText.context.getString(label), block)
    }

    fun addEditorActionListener(block: () -> Unit) {
        addEditorActionListener(null, block)
    }

    fun addEditorActionListener(label: String?, block: () -> Unit) {
        nextLabel = label ?: "Next"
        nextAction = block
    }

    /**
     * Add On Edit Text Click Listener
     */
    fun addEditTextClickListener(view: View? = null, block: ((View?) -> Unit)?) {
        if (null == block) {
            enableFocus()
            CustomKeyboard.showKeyboard(this@InputInterface)
            editText.setOnClickListener(null)
        } else {
            disableFocus()
            editText.setOnClickListener { block(null) }
        }
    }

    fun disableEditText() {
        editText.apply {
            isFocusable = false
            isEnabled = false
            isCursorVisible = false
            setOnKeyListener(null)
        }
    }

    fun enableEditText() {
        editText.apply {
            isFocusable = true
            isEnabled = true
            isCursorVisible = true
        }
    }

    fun disableFocus() {
        editText.apply {
            isFocusable = false
            isCursorVisible = false
        }
    }

    fun enableFocus() {
        editText.apply {
            isFocusable = true
            isCursorVisible = true
        }
    }

    private fun animateShowError(s: String) {
        val anim = getFadeInAnim()
        anim.setAnimationListener(object : SimpleAnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                errorTextView?.apply {
                    text = s
                    visibility = View.VISIBLE
                }
            }
        })
        errorTextView?.startAnimation(anim)
    }

    private fun animateHideError() {
        val s = text.toString()
        if (s.isNullOrEmpty()) return
        val anim = getFadeOutAnim()
        anim.setAnimationListener(object : SimpleAnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                errorTextView?.apply {
                    text = null
                    visibility = View.GONE
                }
            }
        })
        errorTextView?.startAnimation(anim)
    }

    fun getFadeInAnim(): Animation {
        val anim = AlphaAnimation(0f, 1f)
        anim.duration = 800
        anim.fillAfter = true
        return anim
    }

    fun getFadeOutAnim(): Animation {
        val anim = AlphaAnimation(1f, 0f)
        anim.duration = 800
        anim.fillAfter = true
        return anim
    }

    override fun afterTextChanged(p0: Editable?) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    interface SimpleAnimationListener : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
        }

        override fun onAnimationRepeat(p0: Animation?) {
        }
    }
}