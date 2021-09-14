package com.sample.widget.base

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.sample.widget.R
import com.sample.widget.extension.GlideApp

class ProgressImageView : AppCompatImageView {

    companion object {
        val imageRes = R.drawable.ic_adb
        val GIF = 0
        val ANIM = 1
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        if (context is AppCompatActivity) {
            visibility = View.INVISIBLE
        } else {
            setImageResource(imageRes)
        }
    }

    private var style: Int = GIF

    private val defaultAnim: ObjectAnimator
        get() {
            return ObjectAnimator.ofFloat(this, "rotation", 0f, 36000f).apply {
                duration = 200000
                interpolator = DecelerateInterpolator()
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
            }
        }

    private var animation: ObjectAnimator? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (context is AppCompatActivity) when (style) {
            ANIM -> startAnimation()
            GIF -> loadImage()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

    private fun startAnimation() {
        if (animation == null) animation = defaultAnim
        animation?.start()
    }

    private fun stopAnimation() {
        animation?.end()
    }

    private fun loadImage() {
        GlideApp.with(context)
            .load(imageRes)
            .override(width, height)
            .into(this)
    }

    fun show() {
        visibility = View.VISIBLE
    }

    fun hide() {
        visibility = View.INVISIBLE
    }

}