package com.huy.kotlin.widget

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.huy.kotlin.R
import com.huy.library.extension.getSize
import com.huy.library.extension.statusBarHeight
import com.huy.library.widget.AppCustomView
import kotlinx.android.synthetic.main.widget_app_bar.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/12
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class AppBarView : AppCustomView {

    /**
     * [AppCustomView] implement
     */
    override val layoutRes: Int get() = R.layout.widget_app_bar

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun onInitialize(context: Context, types: TypedArray) {

        appBarImageViewBackground.setImageDrawable(types.background)

        textViewTitle.apply {
            text = types.text
            isAllCaps = types.textAllCaps
            val style = this.typeface.style
            setTypeface(textViewTitle.typeface, style)
            setTextColor(types.textColorRes)
        }

        val color = types.textColorRes

        imageViewDrawableStart.apply {
            setColorFilter(color)
            setImageDrawable(types.drawableStart)
        }

        imageViewDrawableEnd.apply {
            setColorFilter(color)
            setImageDrawable(types.drawableEnd)
        }

    }

    /**
     * [AppBarView] properties
     */
    var title: String? = null
        set(value) {
            textViewTitle.text = value
        }

    var backgroundImage: Drawable? = null
        set(value) {
            appBarImageViewBackground.setImageDrawable(value)
        }

    fun startButtonClickListener(block: () -> Unit) {
        imageViewDrawableStart.setOnClickListener { block() }
    }

    fun endButtonClickListener(block: () -> Unit) {
        imageViewDrawableEnd.setOnClickListener { block() }
    }

    var drawableStart: Drawable? = null
        set(value) {
            imageViewDrawableStart.setImageDrawable(value)
        }

    var drawableEnd: Drawable? = null
        set(value) {
            imageViewDrawableEnd.setImageDrawable(value)
        }

    var progressVisible: Boolean
        get() = appBarProgressBar.visibility == View.VISIBLE
        set(value) {
            appBarProgressBar.visibility = if (value) View.VISIBLE else View.INVISIBLE
        }

    fun addPopBackClick() {
        imageViewDrawableStart.apply {
            post {
                setImageResource(R.drawable.ic_back)
                setOnClickListener { v ->
                    (v.context as? Activity)?.onBackPressed()
                }
            }
        }
    }

    private fun updateStatusBar() {
        this.getSize { w, h ->
            val extraHeight = statusBarHeight
            appBarViewControls.setPadding(0, extraHeight, 0, 0)
            val lp = this.layoutParams
            lp.height = h + extraHeight
            this.layoutParams = lp
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE) {
            updateStatusBar()
        }
    }
}
