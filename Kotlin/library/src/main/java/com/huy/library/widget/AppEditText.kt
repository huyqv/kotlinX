package com.huy.library.widget

import android.content.Context
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/08/29
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class AppEditText : AppCompatEditText, TextWatcher {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var ignoreKeyChange: Boolean = false

    private var delayInterval: Long = 800 // handle waiting time after stop typing

    private var lastEditInterval: Long = 0

    private var handle: Handler? = null

    private var onTextChanged: ((String?) -> Unit)? = null

    private var runnable: Runnable? = null

    fun onTextChanged(delayInterval: Long = 800, listener: ((String?) -> Unit)?) {

        this.onTextChanged = listener
        this.delayInterval = delayInterval
        this.handle = Handler()
        this.runnable = Runnable {
            if (System.currentTimeMillis() > lastEditInterval + this.delayInterval - 400) {
                val s = this.text.toString().trim()
                this.onTextChanged?.run { listener!!(s) }

            }
        }
        addTextChangedListener(this)
    }

    fun removeOnTextChanged() {
        removeTextChangedListener(this)
        handle = null
        onTextChanged = null
        runnable = null
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        // Remove line goes here to run only once
        if (null != runnable) {
            handle?.removeCallbacks(runnable)
        }
    }

    override fun afterTextChanged(s: Editable) {
        // Avoid triggering event when text is empty
        if (text.toString().isNullOrEmpty()) {
            onTextChanged?.run { onTextChanged!!(null) }
            return
        }
        if (s.isNotEmpty()) {
            lastEditInterval = System.currentTimeMillis()
            handler.postDelayed(runnable, delayInterval)
        }
    }

}