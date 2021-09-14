package com.sample.widget.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.*
import android.text.Editable
import android.text.InputFilter
import android.util.AttributeSet
import android.util.TypedValue
import android.view.*
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.motion.widget.MotionLayout
import com.sample.widget.R
import com.sample.widget.databinding.AppInputBinding
import com.sample.widget.extension.*

class AppInputView : AppCustomView<AppInputBinding>,
    SimpleMotionTransitionListener,
    OnFocusChangeListener,
    SimpleTextWatcher {

    private val colorFocused get() = R.color.colorPrimary

    private val colorUnFocus get() = android.R.color.darker_gray

    private val colorError get() = android.R.color.holo_red_dark

    private val colorHint get() = android.R.color.darker_gray

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun inflating(): (LayoutInflater, ViewGroup?, Boolean) -> AppInputBinding {
        return AppInputBinding::inflate
    }

    override fun onInitialize(context: Context, types: TypedArray) {
        title = types.title
        bind.inputEditText.setText(types.text)
        bind.inputEditText.addTextChangedListener(this)
        onIconInitialize(types)
        onEditTextInitialize(bind.inputEditText, types)
        bind.inputViewLayout.addTransitionListener(this)
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

    override fun performClick(): Boolean {
        return editText.performClick()
    }

    override fun onDetachedFromWindow() {
        bind.inputViewLayout.clearAnimation()
        onFocusChange.clear()
        super.onDetachedFromWindow()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        superState?.let {
            val state = SaveState(superState)
            state.dataInput = text.toString()
            return state
        } ?: run {
            return superState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        when (state) {
            is SaveState -> {
                super.onRestoreInstanceState(state.superState)
                Handler(Looper.getMainLooper()).post {
                    text = state.dataInput
                }
            }
        }
    }

    private fun onIconInitialize(types: TypedArray) {
        val color = types.getColor(R.styleable.AppCustomView_android_tint, -1)
        if (color != -1) {
            bind.inputImageViewIcon.setColorFilter(color)
        }
        src = types.srcRes
    }

    private fun onEditTextInitialize(it: AppCompatEditText, types: TypedArray) {
        it.onFocusChangeListener = this
        it.paintFlags = it.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
        it.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            types.getDimension(
                R.styleable.AppCustomView_android_textSize,
                getPixels(R.dimen.textSize2)
            )
        )
        it.maxLines = 1

        // Text filter
        val sFilters = arrayListOf<InputFilter>()

        val textAllCaps = types.getBoolean(R.styleable.AppCustomView_android_textAllCaps, false)
        if (textAllCaps) sFilters.add(InputFilter.AllCaps())

        val sMaxLength = types.getInt(R.styleable.AppCustomView_android_maxLength, 256)
        sFilters.add(InputFilter.LengthFilter(sMaxLength))

        val array = arrayOfNulls<InputFilter>(sFilters.size)
        it.filters = sFilters.toArray(array)

        // Input type
        val attrInputType = types.getInt(
            R.styleable.AppCustomView_android_inputType,
            EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        )
        when (attrInputType) {
            EditorInfo.TYPE_NULL -> {
                disableFocus()
            }
            EditorInfo.TYPE_CLASS_NUMBER -> {
                editText.addFilter(DIGIT_CHARS)
                it.inputType = attrInputType
            }
            else -> {
                it.inputType =
                    attrInputType or EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS or EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING
            }
        }

        it.maxLines = types.getInt(R.styleable.AppCustomView_android_maxLines, 1)

        // Ime option
        val imeOption = types.getInt(R.styleable.AppCustomView_android_imeOptions, -1)
        if (imeOption != -1) it.imeOptions = imeOption

        it.privateImeOptions = "nm,com.google.android.inputmethod.latin.noMicrophoneKey"

        // Gesture
        it.setOnLongClickListener {
            return@setOnLongClickListener true
        }
        it.setTextIsSelectable(false)
        it.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu) = false
            override fun onDestroyActionMode(mode: ActionMode) {}
            override fun onCreateActionMode(mode: ActionMode, menu: Menu) = false
            override fun onActionItemClicked(mode: ActionMode, item: MenuItem) = false
        }
        it.isLongClickable = false
        it.setOnCreateContextMenuListener { menu, _, _ -> menu.clear() }
    }

    /**
     * [AppInputView] properties
     */
    private val editText: EditText get() = bind.inputEditText

    private val parentBackgroundColor: Int get() = (parent as? View)?.backgroundColor ?: Color.WHITE

    var text: String?
        get() {
            isSilent = true
            val s = editText.text?.toString()?.trimText
            isSilent = false
            return s
        }
        set(value) {
            isSilent = true
            editText.setText(value)
            error = null
            onFocusChange(null, isFocused)
            isSilent = false
        }

    val trimText: String
        get() {
            isSilent = true
            val s = editText.trimText
            isSilent = false
            return s
        }

    var title: String?
        get() = bind.inputTextViewTitle.text?.toString()
        set(value) {
            bind.inputTextViewTitle.text = value
        }

    var error: String?
        get() = bind.inputTextViewError.text?.toString()
        set(value) {
            bind.inputTextViewError.text = value
            updateUiOnFocusChanged()
        }

    @DrawableRes
    var src: Int = 0
        set(value) {
            val isGone = value <= 0
            bind.inputImageViewIcon.isGone(isGone)
            bind.inputImageViewIcon.setImageResource(value)
        }

    var isSilent: Boolean = false

    val isTextEmpty: Boolean get() = text.isNullOrEmpty()

    val hasError: Boolean get() = !error.isNullOrEmpty()

    val textLength: Int get() = text?.length ?: 0

    /**
     * [OnFocusChangeListener] implements
     */
    private val onFocusChange = mutableListOf<(Boolean) -> Unit>()

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        onFocusChange.iterator().forEach {
            it(hasFocus)
        }
        updateUiOnFocusChanged(hasFocus)
    }

    override fun hasFocusable(): Boolean {
        return false
    }

    override fun isFocused(): Boolean {
        return false
    }

    override fun hasFocus(): Boolean {
        return editText.hasFocus()
    }

    override fun clearFocus() {
        editText.clearFocus()
        hideKeyboard()
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        editText.post {
            if (!editText.isFocused) {
                editText.requestFocus(FOCUS_DOWN)
                showKeyboard()
            }
        }
        return true
    }

    fun addOnFocusChangeListener(block: (Boolean) -> Unit) {
        onFocusChange.add(block)
    }

    fun addActionDoneListener(block: (String?) -> Unit) {
        editText.addActionDoneListener(block)
    }

    fun addActionNextListener(block: (String?) -> Unit) {
        editText.addActionNextListener(block)
    }

    fun disableFocus() {
        editText.also {
            it.isFocusable = false
            it.isCursorVisible = false
        }
    }

    fun enableFocus() {
        editText.also {
            it.isFocusable = true
            it.isCursorVisible = true
        }
    }

    fun clear() {
        editText.text = null
        error = null
        updateUiOnFocusChanged()
    }

    /**
     * [SimpleMotionTransitionListener] implement
     */
    override fun onTransitionCompleted(layout: MotionLayout, currentId: Int) {
        when (currentId) {
            R.id.focused -> {
                setTitleBackground(parentBackgroundColor)
            }
        }
    }

    /**
     * [SimpleTextWatcher] implements
     */
    var onTextChanged: ((String) -> Unit)? = null

    override fun afterTextChanged(s: Editable?) {
        onTextChanged?.invoke(s.toString())
        when {
            isSilent -> {
                return
            }
            hasError && !text.isNullOrEmpty() -> {
                error = null
            }
        }
    }

    /**
     * ui state on focus change, error change, text change
     */
    private fun updateUiOnFocusChanged(hasFocus: Boolean = editText.hasFocus()) {
        when {
            hasFocus -> {
                if (editText.isFocusable) {
                    editText.select()
                    showKeyboard()
                }
                setInputBackground(0)
                setMotionState(R.id.focused)
                if (error.isNullOrEmpty()) {
                    setTitleColor(colorFocused)
                    setBorderColor(colorFocused)
                    setIconColor(colorFocused)
                    setInputBackground(0)
                } else {
                    setTitleColor(colorError)
                    setBorderColor(colorError)
                    setIconColor(colorError)
                }
            }
            !hasFocus && text.isNullOrEmpty() -> {
                setTitleColor(colorHint)
                setTitleBackground(Color.TRANSPARENT)
                setInputBackground(R.drawable.drw_input_bg)
                setMotionState(R.id.unfocused)
                if (error.isNullOrEmpty()) {
                    setBorderColor(colorUnFocus)
                    setIconColor(colorUnFocus)
                } else {
                    setBorderColor(colorError)
                    setIconColor(colorError)
                }
            }
            !hasFocus && !text.isNullOrEmpty() -> {
                setTitleBackground(parentBackgroundColor)
                setInputBackground(0)
                setMotionState(R.id.focused)
                if (error.isNullOrEmpty()) {
                    setTitleColor(colorFocused)
                    setBorderColor(colorUnFocus)
                    setIconColor(colorUnFocus)
                } else {
                    setTitleColor(colorError)
                    setBorderColor(colorError)
                    setIconColor(colorError)
                }
            }
        }
    }

    fun showKeyboard() {
        editText.post {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }
    }

    fun hideKeyboard() {
        post {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(editText.windowToken, 0)
        }
    }

    fun addDateWatcher() {
        addOnFocusChangeListener {
            editText.hint = if (it) {
                "dd/MM/yyyy"
            } else {
                null
            }
        }
        editText.addDateWatcher()
    }

    fun filter(filterChars: CharArray) {
        val arrayList = arrayListOf<InputFilter>()
        editText.filters?.apply { arrayList.addAll(this) }
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
        editText.filters = arrayList.toArray(arrayOfNulls<InputFilter>(arrayList.size))
    }

    private fun setMotionState(id: Int) {
        bind.inputViewLayout.transitionToState(id)
    }

    private fun setBorderColor(@ColorRes res: Int) {
        bind.inputEditText.backgroundTintRes(res)
    }

    private fun setIconColor(@ColorRes res: Int) {
        bind.inputImageViewIcon.tintRes(res)
    }

    private fun setTitleColor(@ColorRes res: Int) {
        bind.inputTextViewTitle.textColorRes(res)
    }

    private fun setInputBackground(@DrawableRes res: Int) {
        bind.inputViewBackground.setBackgroundResource(res)
    }

    private fun setTitleBackground(@ColorInt color: Int) {
        bind.inputTextViewTitle.setBackgroundColor(color)
    }

    inner class SaveState : AbsSavedState {

        var dataInput: String? = null

        constructor(superState: Parcelable) : super(superState)

        @RequiresApi(Build.VERSION_CODES.N)
        constructor(source: Parcel, loader: ClassLoader?) : super(source, loader) {
            dataInput = source.readString()
        }

        override fun writeToParcel(dest: Parcel?, flags: Int) {
            super.writeToParcel(dest, flags)
            dest?.writeString(dataInput)
        }
    }

}