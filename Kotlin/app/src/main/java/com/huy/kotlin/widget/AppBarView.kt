package com.huy.kotlin.widget

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.animation.StateListAnimator
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.huy.kotlin.R
import com.huy.library.extension.setStatusBarBackground
import kotlinx.android.synthetic.main.widget_app_bar.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/12
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class AppBarView : FrameLayout {

    private val stateListAnimators = intArrayOf(android.R.attr.stateListAnimator)

    var title: String? = null
        set(value) {
            textViewTitle.text = value
        }

    fun leftIconClickListener(block: () -> Unit) {
        imageViewDrawableStart.setOnClickListener { block() }
    }

    fun rightIconClickListener(block: () -> Unit) {
        imageViewDrawableEnd.setOnClickListener { block() }
    }

    var drawableStart: Int = 0
        set(@DrawableRes value) {
            imageViewDrawableStart.setImageResource(value)
        }

    var drawableEnd: Int = 0
        set(@DrawableRes value) {
            imageViewDrawableEnd.setImageResource(value)
        }

    var progressVisible: Boolean
        get() = progressBar.visibility == View.VISIBLE
        set(value) {
            progressBar.visibility = if (value) View.VISIBLE else View.INVISIBLE
        }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        val types = context.theme.obtainStyledAttributes(attrs, R.styleable.AppBarView, 0, 0)
        LayoutInflater.from(context).inflate(R.layout.widget_app_bar, this)

        try {

            types.configTitle()
            types.configBackground()

            val tintEnable = types.getBoolean(R.styleable.AppBarView_appBar_tintEnable, true)
            val tintId = types.getResourceId(R.styleable.AppBarView_android_tint, android.R.color.white)
            val tint = ContextCompat.getColor(context, tintId)

            types.configDrawable(tint, tintEnable)

            types.configActionBack(tint, tintEnable)

            types.configElevation()

        } finally {
            types.recycle()
        }
    }

    private fun TypedArray.configTitle() {

        textViewTitle.apply {
            text = getString(R.styleable.AppBarView_android_text)
            isAllCaps = getBoolean(R.styleable.AppBarView_android_textAllCaps, false)
            val style = getInt(R.styleable.AppBarView_android_textStyle, Typeface.NORMAL)
            setTypeface(textViewTitle.typeface, style)
            val color = getResourceId(R.styleable.AppBarView_android_textColor, android.R.color.white)
            setTextColor(ContextCompat.getColor(context, color))
        }
    }

    private fun TypedArray.configBackground() {

        val background = getResourceId(R.styleable.AppBarView_android_background, R.color.colorPrimary)
        viewAppBar.setBackgroundResource(background)
        (context as? Activity)?.setStatusBarBackground(background)
    }

    private fun TypedArray.configActionBack(tint: Int, tintEnable: Boolean) {

        if (getBoolean(R.styleable.AppBarView_appBar_actionBack, true))
            imageViewDrawableStart.apply {
                if (tintEnable) setColorFilter(tint)
                setImageResource(R.drawable.ic_back)
                setOnClickListener { v ->
                    (v.context as? Activity)?.onBackPressed()
                }
            }
    }

    private fun TypedArray.configDrawable(tint: Int, tintEnable: Boolean) {

        val left = getResourceId(R.styleable.AppBarView_android_drawableStart, 0)
        if (left != 0) imageViewDrawableStart.apply {
            if (tintEnable) setColorFilter(tint)
            setImageResource(left)
        }

        val right = getResourceId(R.styleable.AppBarView_android_drawableEnd, 0)
        if (right != 0) imageViewDrawableEnd.apply {
            if (tintEnable) setColorFilter(tint)
            setImageResource(right)
        }
    }

    private fun TypedArray.configElevation() {
        if (Build.VERSION.SDK_INT < 21) return
        if (getBoolean(R.styleable.AppBarView_appBar_mostForward, false)) {
            setStateListAnimatorFromAttrs(this@AppBarView, null, 0, R.style.Widget_Design_AppBarLayout)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setStateListAnimator(view: View, elevation: Float) {

        val dur = view.resources.getInteger(R.integer.app_bar_elevation_anim_duration)
        val sla = StateListAnimator()
        sla.addState(intArrayOf(android.R.attr.enabled, R.attr.state_liftable, -R.attr.state_lifted), ObjectAnimator.ofFloat(this, "elevation", 0f).setDuration(dur.toLong()))
        sla.addState(intArrayOf(android.R.attr.enabled), ObjectAnimator.ofFloat(view, "elevation", elevation).setDuration(dur.toLong()))
        sla.addState(IntArray(0), ObjectAnimator.ofFloat(view, "elevation", 0F).setDuration(0))
        this.stateListAnimator = sla
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setStateListAnimatorFromAttrs(view: View, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {

        view.outlineProvider = ViewOutlineProvider.BOUNDS
        val context = view.context
        val a = context.obtainStyledAttributes(attrs, stateListAnimators, defStyleAttr, defStyleRes)
        try {
            if (a.hasValue(0)) {
                val sla = AnimatorInflater.loadStateListAnimator(context, a.getResourceId(0, 0))
                view.stateListAnimator = sla
            }
        } finally {
            a.recycle()
        }
    }

    fun setTargetElevation(elevation: Float) {
        if (Build.VERSION.SDK_INT >= 21) setStateListAnimator(this, elevation)
    }

}
