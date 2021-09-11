package com.sample.widget.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.viewbinding.ViewBinding
import com.sample.widget.R

abstract class AppCustomView<B : ViewBinding> : ConstraintLayout {

    protected abstract fun onInitialize(context: Context, types: TypedArray)

    protected open fun styleResource(): IntArray {
        return R.styleable.AppCustomView
    }

    protected lateinit var bind: B

    abstract fun inflating(): (LayoutInflater, ViewGroup, Boolean) -> B

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        onViewInit(context, attrs)
    }

    private fun onViewInit(context: Context, attrs: AttributeSet?) {
        val types = context.theme.obtainStyledAttributes(attrs, styleResource(), 0, 0)
        try {
            bind = inflating().invoke(LayoutInflater.from(context), this, true)
            onInitialize(context, types)
        } finally {
            types.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        println("onFinishInflate")
    }

    /**
     * Text
     */
    val TypedArray.text: String?
        get() = getString(R.styleable.AppCustomView_android_text)

    val TypedArray.title: String?
        get() = getString(R.styleable.AppCustomView_android_title)

    val TypedArray.hint: String?
        get() = getString(R.styleable.AppCustomView_android_hint)

    /**
     * Input type
     */
    val TypedArray.maxLength: Int
        get() = getInt(R.styleable.AppCustomView_android_maxLength, 256)

    val TypedArray.maxLines: Int
        get() = getInt(R.styleable.AppCustomView_android_maxLines, 1)

    val TypedArray.textAllCaps: Boolean
        get() = getBoolean(R.styleable.AppCustomView_android_textAllCaps, false)

    /**
     * Color
     */
    val TypedArray.tint: Int
        get() {
            return getColor(R.styleable.AppCustomView_android_tint, Color.BLACK)
        }

    /*val TypedArray.drawableTint: Int
        get() {
            return getColor(R.styleable.AppCustomView_android_drawableTint, Color.BLACK)
        }*/

    val TypedArray.backgroundTint: Int
        get() {
            return getColor(R.styleable.AppCustomView_android_backgroundTint, Color.WHITE)
        }

    val TypedArray.textColor: Int
        get() {
            return getColor(R.styleable.AppCustomView_android_textColor, Color.BLACK)
        }

    val TypedArray.textColorHint: Int
        get() {
            return getColor(R.styleable.AppCustomView_android_textColorHint, Color.BLACK)
        }

    val TypedArray.hintColor: Int
        get() {
            return getColor(R.styleable.AppCustomView_android_textColor, Color.DKGRAY)
        }

    /**
     * Drawable
     */
    val TypedArray.drawableStart: Drawable?
        get() {
            return getDrawable(R.styleable.AppCustomView_android_drawableStart)
                ?.constantState?.newDrawable()?.mutate()
        }

    val TypedArray.drawableEnd: Drawable?
        get() {
            return getDrawable(R.styleable.AppCustomView_android_drawableEnd)
                ?.constantState?.newDrawable()?.mutate()
        }

    val TypedArray.drawable: Drawable?
        get() {
            return getDrawable(R.styleable.AppCustomView_android_drawable)
                ?.constantState?.newDrawable()?.mutate()
        }

    val TypedArray.src: Drawable?
        get() {
            return getDrawable(R.styleable.AppCustomView_android_src)
                ?.constantState?.newDrawable()?.mutate()
        }

    val TypedArray.srcRes: Int
        get() {
            return getResourceId(R.styleable.AppCustomView_android_src, 0)
        }

    val TypedArray.background: Int
        get() {
            return getResourceId(R.styleable.AppCustomView_android_background, 0)
        }

    /**
     * Checkable
     */
    val TypedArray.checkable: Boolean
        get() = getBoolean(R.styleable.AppCustomView_android_checkable, false)

    val TypedArray.checked: Boolean
        get() = getBoolean(R.styleable.AppCustomView_android_checked, false)

    /**
     * Padding
     */
    val TypedArray.paddingStart: Int
        get() = getDimensionPixelSize(R.styleable.AppCustomView_android_paddingStart, 0)

    val TypedArray.paddingEnd: Int
        get() = getDimensionPixelSize(R.styleable.AppCustomView_android_paddingEnd, 0)

    val TypedArray.paddingTop: Int
        get() = getDimensionPixelSize(R.styleable.AppCustomView_android_paddingTop, 0)

    val TypedArray.paddingBottom: Int
        get() = getDimensionPixelSize(R.styleable.AppCustomView_android_paddingBottom, 0)

    fun TypedArray.pixels(@StyleableRes id: Int) {
        getDimensionPixelSize(id, 0).toFloat()
    }

    /**
     * Utils
     */
    fun getPixels(@DimenRes res: Int): Float {
        return context.resources.getDimensionPixelSize(res).toFloat()
    }

    fun anim(@AnimRes res: Int): Animation {
        return AnimationUtils.loadAnimation(context, res)
    }

    fun drawable(@DrawableRes res: Int): Drawable {
        return ContextCompat.getDrawable(context, res)!!
    }

    fun createDrawable(@DrawableRes res: Int): Drawable? {
        return drawable(res).constantState?.newDrawable()?.mutate()
    }

    fun Drawable?.tint(@ColorInt color: Int): Drawable? {
        this ?: return null
        DrawableCompat.setTint(this, color)
        DrawableCompat.setTintMode(this, PorterDuff.Mode.SRC_IN)
        return this
    }

    fun Drawable?.tintRes(@ColorRes color: Int): Drawable? {
        return tint(ContextCompat.getColor(context, color))
    }

    fun pixels(@DimenRes res: Int): Float {
        return context.resources.getDimensionPixelSize(res).toFloat()
    }

    fun color(@ColorRes res: Int): Int {
        return ContextCompat.getColor(context, res)
    }

    fun string(@StringRes res: Int): String {
        return context.getString(res)
    }

    fun string(@StringRes res: Int, vararg args: Any?): String {
        return try {
            String.format(context.getString(res), *args)
        } catch (ignore: Exception) {
            ""
        }
    }

    fun View.backgroundTint(@ColorInt color: Int) {
        post {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                background?.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
            } else {
                @Suppress("DEPRECATION")
                background?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
        }
    }

    fun View.backgroundTintRes(@ColorRes colorRes: Int) {
        backgroundTint(color(colorRes))
    }

    fun ImageView.tint(@ColorInt color: Int) {
        post {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
            } else {
                @Suppress("DEPRECATION")
                setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
        }
    }

    fun ImageView.tintRes(@ColorRes res: Int) {
        tint(ContextCompat.getColor(context, res))
    }

    fun ImageView.postImage(@DrawableRes drawableRes: Int) {
        post { this.setImageResource(drawableRes) }
    }

    fun TextView.textColor(@ColorRes color: Int) {
        setTextColor(ContextCompat.getColor(context, color))
    }
}