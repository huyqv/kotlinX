package com.sample.widget.custom

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.sample.widget.R
import com.sample.widget.databinding.AppBarBinding
import com.sample.widget.extension.getSize

class AppBarView : AppCustomView<AppBarBinding> {

    override fun inflating(): (LayoutInflater, ViewGroup, Boolean) -> ViewBinding {
        return AppBarBinding::inflate
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun onInitialize(context: Context, types: TypedArray) {

        bind.imageViewBackground.setImageResource(types.background)

        bind.textViewTitle.also {
            it.text = types.text
            it.isAllCaps = types.textAllCaps
            it.setTypeface(it.typeface, it.typeface.style)
            it.setTextColor(types.textColor)
            it.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                types.getDimension(R.styleable.AppCustomView_android_textSize, 9F)
            )
        }

        bind.imageViewDrawableStart.also {
            it.setColorFilter(types.textColor)
            it.setImageDrawable(types.drawableStart)
        }

        bind.imageViewDrawableEnd.also {
            it.setColorFilter(types.textColor)
            it.setImageDrawable(types.drawableEnd)
        }
    }

    /**
     * [AppBarView] properties
     */
    var title: String? = null
        set(value) {
            bind.textViewTitle.text = value
        }

    var backgroundImage: Drawable? = null
        set(value) {
            bind.imageViewBackground.setImageDrawable(value)
        }

    fun startButtonClickListener(block: () -> Unit) {
        bind.imageViewDrawableStart.setOnClickListener { block() }
    }

    fun endButtonClickListener(block: () -> Unit) {
        bind.imageViewDrawableEnd.setOnClickListener { block() }
    }

    var drawableStart: Drawable? = null
        set(value) {
            bind.imageViewDrawableStart.setImageDrawable(value)
        }

    var drawableEnd: Drawable? = null
        set(value) {
            bind.imageViewDrawableEnd.setImageDrawable(value)
        }

    var progressVisible: Boolean
        get() = bind.appBarProgressBar.visibility == View.VISIBLE
        set(value) {
            bind.appBarProgressBar.visibility = if (value) View.VISIBLE else View.INVISIBLE
        }

    val statusBarHeight: Int
        get() {
            var result = 0
            val res = context.resources
            val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) result = res.getDimensionPixelSize(resourceId)
            return result
        }

    fun addPopBackClick() {
        bind.imageViewDrawableStart.apply {
            post {
                setImageResource(R.drawable.ic_back)
                setOnClickListener { v ->
                    (v.context as? Activity)?.onBackPressed()
                }
            }
        }
    }

    fun updateStatusBar() {
        this.getSize { _, h ->
            val extraHeight = statusBarHeight
            bind.layoutContent.setPadding(0, extraHeight, 0, 0)
            val lp = this.layoutParams
            lp.height = h + extraHeight
            this.layoutParams = lp
            bind.layoutContent.invalidate()
        }
    }
}
