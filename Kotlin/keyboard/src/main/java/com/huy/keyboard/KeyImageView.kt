package com.huy.keyboard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat

class KeyImageView : AppCompatImageView {

    private var mIsChecked: Boolean = false

    fun silentCheck(value: Boolean) {
        mIsChecked = value
    }

    var isChecked: Boolean
        get() = mIsChecked
        set(value) {
            if (value == mIsChecked) return
            mIsChecked = value
            post {
                val tintColor = ContextCompat.getColor(context!!, if (isChecked) android.R.color.white else R.color.colorKeyText)
                setColorFilter(tintColor)
                val backgroundColor = if (isChecked) R.drawable.bg_key_blue else R.drawable.bg_key_white
                setBackgroundResource(backgroundColor)
                checkedChangeListener?.run { checkedChangeListener!!(this@KeyImageView, isChecked) }
            }
        }

    private var checkedChangeListener: ((View, Boolean) -> Unit)? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        super.setOnClickListener {
            isChecked = !isChecked
        }
    }

    fun setOnCheckedChangeListener(listener: (View, Boolean) -> Unit) {
        checkedChangeListener = listener
    }


}