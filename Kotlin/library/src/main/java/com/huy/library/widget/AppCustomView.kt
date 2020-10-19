package com.huy.library.widget

import android.app.Application
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.huy.library.Library
import com.huy.library.R
import com.huy.library.extension.createDrawable

abstract class AppCustomView : FrameLayout {

    protected abstract fun onInitialize(context: Context, types: TypedArray)

    protected open fun styleResource(): IntArray {
        return R.styleable.CustomView
    }

    protected abstract fun layoutResource(): Int

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        onViewInit(context, attrs)
    }

    private fun onViewInit(context: Context, attrs: AttributeSet?) {
        val types = context.theme.obtainStyledAttributes(attrs, styleResource(), 0, 0)
        try {
            LayoutInflater.from(context).inflate(layoutResource(), this)
            onInitialize(context, types)
        } finally {
            types.recycle()
        }

    }

    private val app: Application get() = Library.app


    /**
     * Text
     */
    val TypedArray.text: String?
        get() = getString(R.styleable.CustomView_android_text)

    val TypedArray.title: String?
        get() = getString(R.styleable.CustomView_android_title)

    val TypedArray.hint: String?
        get() = getString(R.styleable.CustomView_android_hint)

    /**
     * Input type
     */
    val TypedArray.maxLength: Int
        get() = getInt(R.styleable.CustomView_android_maxLength, 256)

    val TypedArray.maxLines: Int
        get() = getInt(R.styleable.CustomView_android_maxLines, 1)

    val TypedArray.textAllCaps: Boolean
        get() = getBoolean(R.styleable.CustomView_android_textAllCaps, false)

    /**
     * Color
     */
    val TypedArray.tint: Int
        get() {
            return getColor(R.styleable.CustomView_android_tint, Color.BLACK)
        }

    val TypedArray.drawableTint: Int
        get() {
            return getColor(R.styleable.CustomView_android_drawableTint, Color.BLACK)
        }

    val TypedArray.backgroundTint: Int
        get() {
            return getColor(R.styleable.CustomView_android_backgroundTint, Color.WHITE)
        }

    val TypedArray.textColor: Int
        get() {
            return getColor(R.styleable.CustomView_android_textColor, Color.BLACK)
        }

    val TypedArray.hintColor: Int
        get() {
            return getColor(R.styleable.CustomView_android_textColor, Color.DKGRAY)
        }

    /**
     * Drawable
     */
    val TypedArray.drawableStart: Drawable?
        get() {
            return getDrawable(R.styleable.CustomView_android_drawableStart)
                    ?.constantState?.newDrawable()?.mutate()
        }

    val TypedArray.drawableEnd: Drawable?
        get() {
            return getDrawable(R.styleable.CustomView_android_drawableEnd)
                    ?.constantState?.newDrawable()?.mutate()
        }

    val TypedArray.drawable: Drawable?
        get() {
            return getDrawable(R.styleable.CustomView_android_drawable)
                    ?.constantState?.newDrawable()?.mutate()
        }

    val TypedArray.src: Drawable?
        get() {
            return getDrawable(R.styleable.CustomView_android_src)
                    ?.constantState?.newDrawable()?.mutate()
        }

    val TypedArray.background: Int
        get() {
            return getResourceId(R.styleable.CustomView_android_background, 0)
        }

    val TypedArray.backgroundDrawable: Drawable?
        get() {
            val resId = background
            if (resId == 0) return null
            return createDrawable(resId)
        }

    /**
     * Checkable
     */
    val TypedArray.checkable: Boolean
        get() = getBoolean(R.styleable.CustomView_android_checkable, false)

    val TypedArray.checked: Boolean
        get() = getBoolean(R.styleable.CustomView_android_checked, false)

}


