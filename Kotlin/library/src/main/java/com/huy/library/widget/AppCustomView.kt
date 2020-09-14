package com.huy.library.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.huy.library.R

abstract class AppCustomView : FrameLayout {

    protected abstract fun onInitialize(context: Context, types: TypedArray)

    protected open val styleRes: IntArray = R.styleable.CustomView

    protected abstract val layoutRes: Int

    constructor(context: Context) : super(context) {
        onViewInit(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        onViewInit(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        onViewInit(context, attrs)
    }

    private fun onViewInit(context: Context, attrs: AttributeSet?) {
        val types = context.theme.obtainStyledAttributes(attrs, styleRes, 0, 0)
        try {
            LayoutInflater.from(context).inflate(layoutRes, this)
            onInitialize(context, types)
        } finally {
            types.recycle()
        }
    }

    val TypedArray.text: String?
        get() = getString(R.styleable.CustomView_android_text)

    val TypedArray.title: String?
        get() = getString(R.styleable.CustomView_android_title)

    val TypedArray.hint: String?
        get() = getString(R.styleable.CustomView_android_hint)

    val TypedArray.drawableStart: Drawable?
        get() {
            val id = getResourceId(R.styleable.CustomView_android_drawableStart, 0)
            if (id == 0) return null
            return ContextCompat.getDrawable(context, id)
        }
    val TypedArray.drawableEnd: Drawable?
        get() {
            val id = getResourceId(R.styleable.CustomView_android_drawableEnd, 0)
            if (id == 0) return null
            return ContextCompat.getDrawable(context, id)
        }

    val TypedArray.drawable: Drawable?
        get() {
            val id = getResourceId(R.styleable.CustomView_android_drawable, 0)
            if (id == 0) return null
            return ContextCompat.getDrawable(context, id)
        }

    val TypedArray.src: Drawable?
        get() {
            val id = getResourceId(R.styleable.CustomView_android_src, 0)
            if (id == 0) return null
            return ContextCompat.getDrawable(context, id)
        }

    val TypedArray.background: Drawable?
        get() {
            val id = getResourceId(R.styleable.CustomView_android_background, 0)
            if (id == 0) return null
            return ContextCompat.getDrawable(context, id)
        }

    val TypedArray.tint: Int
        get() {
            val id = getResourceId(R.styleable.CustomView_android_tint, android.R.color.black)
            return ContextCompat.getColor(context, id)
        }

    val TypedArray.textColorRes: Int
        get() = getColor(R.styleable.CustomView_android_textColor, Color.BLACK)

    val TypedArray.textColor: Int
        get() = ContextCompat.getColor(context, textColorRes)

    val TypedArray.hintColor: Int
        get() {
            val res = getResourceId(R.styleable.CustomView_android_textColorHint, R.color.colorDark)
            return ContextCompat.getColor(context, res)
        }

    val TypedArray.maxLength: Int
        get() = getInt(R.styleable.CustomView_android_maxLength, 256)

    val TypedArray.maxLines: Int
        get() = getInt(R.styleable.CustomView_android_maxLines, 1)

    val TypedArray.textAllCaps: Boolean
        get() = getBoolean(R.styleable.CustomView_android_textAllCaps, false)

    val TypedArray.checkable: Boolean
        get() = getBoolean(R.styleable.CustomView_android_checkable, false)

    val TypedArray.checked: Boolean
        get() = getBoolean(R.styleable.CustomView_android_checked, false)


}