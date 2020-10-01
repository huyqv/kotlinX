package com.huy.library.widget

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.huy.library.Library
import com.huy.library.R

abstract class AppCustomView : FrameLayout {

    protected abstract fun onInitialize(context: Context, types: TypedArray)

    protected open val styleRes: IntArray get() = R.styleable.CustomView

    protected abstract val layoutRes: Int

    constructor(context: Context) : super(context) {
        onViewInit(context, null)
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        onViewInit(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
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
     * Drawable
     */
    val TypedArray.drawableStart: Drawable?
        get() {
            val id = getResourceId(R.styleable.CustomView_android_drawableStart, 0)
            if (id == 0) return null
            return ContextCompat.getDrawable(app, id)?.constantState?.newDrawable()?.mutate()
        }

    val TypedArray.drawableEnd: Drawable?
        get() {
            val id = getResourceId(R.styleable.CustomView_android_drawableEnd, 0)
            if (id == 0) return null
            return ContextCompat.getDrawable(app, id)?.constantState?.newDrawable()?.mutate()
        }

    val TypedArray.drawable: Drawable?
        get() {
            val id = getResourceId(R.styleable.CustomView_android_drawable, 0)
            if (id == 0) return null
            return ContextCompat.getDrawable(app, id)?.constantState?.newDrawable()?.mutate()
        }

    val TypedArray.src: Drawable?
        get() {
            val id = getResourceId(R.styleable.CustomView_android_src, 0)
            if (id == 0) return null
            return ContextCompat.getDrawable(app, id)
        }

    val TypedArray.background: Drawable?
        get() {
            val id = getResourceId(R.styleable.CustomView_android_background, 0)
            if (id == 0) return null
            return ContextCompat.getDrawable(app, id)
        }


    /**
     * Color
     */
    val TypedArray.tint: Int
        get() {
            val id = getResourceId(R.styleable.CustomView_android_tint, android.R.color.black)
            return ContextCompat.getColor(app, id)
        }

    val TypedArray.drawableTint: Int
        get() {
            val id = getResourceId(R.styleable.CustomView_android_drawableTint, android.R.color.black)
            return ContextCompat.getColor(app, id)
        }

    val TypedArray.backgroundTint: Int
        get() {
            val id = getResourceId(R.styleable.CustomView_android_backgroundTint, android.R.color.white)
            return ContextCompat.getColor(app, id)
        }

    val TypedArray.textColorRes: Int
        get() = getResourceId(R.styleable.CustomView_android_textColor, android.R.color.black)

    val TypedArray.textColor: Int
        get() {
            return try {
                val res = getResourceId(R.styleable.CustomView_android_textColor, android.R.color.black)
                ContextCompat.getColor(app, res)
            } catch (e: Resources.NotFoundException) {
                getColor(R.styleable.CustomView_android_textColor, Color.BLACK)
            } catch (e: Exception) {
                Color.BLACK
            }
        }

    val TypedArray.hintColor: Int
        get() {
            val res = getResourceId(R.styleable.CustomView_android_textColorHint, R.color.colorDark)
            return ContextCompat.getColor(app, res)
        }

    /**
     * Editable
     */
    val TypedArray.maxLength: Int
        get() = getInt(R.styleable.CustomView_android_maxLength, 256)

    val TypedArray.maxLines: Int
        get() = getInt(R.styleable.CustomView_android_maxLines, 1)

    val TypedArray.textAllCaps: Boolean
        get() = getBoolean(R.styleable.CustomView_android_textAllCaps, false)

    /**
     * Checkable
     */
    val TypedArray.checkable: Boolean
        get() = getBoolean(R.styleable.CustomView_android_checkable, false)

    val TypedArray.checked: Boolean
        get() = getBoolean(R.styleable.CustomView_android_checked, false)

}


