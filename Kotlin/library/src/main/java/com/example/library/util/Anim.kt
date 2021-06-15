package com.example.library.util

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.*

object Anim {

    private val overshootInterpolator by lazy { OvershootInterpolator() }

    private val accelerateDecelerateInterpolator by lazy { AccelerateDecelerateInterpolator() }

    fun translateHorizontal(fromX: Int, toX: Int, duration: Long): Animation {
        val anim = TranslateAnimation(fromX.toFloat(), toX.toFloat(), 0f, 0f)
        anim.duration = duration
        anim.fillAfter = true
        return anim
    }

    fun translateVertically(fromY: Int, toY: Int, duration: Long): Animation {
        val anim = TranslateAnimation(0f, 500f, fromY.toFloat(), toY.toFloat())
        anim.duration = duration
        anim.fillAfter = true
        return anim
    }

    fun fadeIn(duration: Long = 500, fillAfter: Boolean = true): Animation {
        val anim = AlphaAnimation(0f, 1f)
        anim.duration = duration
        anim.fillAfter = fillAfter
        return anim
    }

    fun fadeOut(duration: Long = 500, fillAfter: Boolean = true): Animation {
        val anim = AlphaAnimation(0f, 0f)
        anim.duration = duration
        anim.fillAfter = fillAfter
        return anim
    }

    fun scaleIn(duration: Long = 500, fillAfter: Boolean = true): Animation {
        val anim = ScaleAnimation(1f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f)
        anim.duration = duration
        anim.fillAfter = fillAfter
        return anim
    }

    fun scaleOut(duration: Long = 500, fillAfter: Boolean = true): Animation {
        val anim = ScaleAnimation(0f, 0f, 0f, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f)
        anim.duration = duration
        anim.fillAfter = fillAfter
        return anim
    }

    fun rotate(infinite: Boolean = true): Animation {
        val anim = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        if (infinite) anim.repeatCount = Animation.INFINITE
        anim.duration = 700
        return anim
    }

    fun rotate(toDegrees: Float = 3600f, duration: Long = 1000, infinite: Boolean): Animation {
        val anim = RotateAnimation(0f, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        if (infinite) anim.repeatCount = Animation.INFINITE
        anim.duration = duration
        return anim
    }

    fun rotateAxisX(view: View, infinite: Boolean = true): ObjectAnimator {
        val anim = ObjectAnimator.ofFloat(view, "rotationY", 0.0f, 360f)
        anim.interpolator = accelerateDecelerateInterpolator
        if (infinite) anim.repeatCount = Animation.INFINITE
        anim.duration = 2500
        return anim
    }

    fun rotateAxisY(view: View, infinite: Boolean = true): ObjectAnimator {
        val anim = ObjectAnimator.ofFloat(view, "rotationX", 0.0f, 360f)
        anim.interpolator = accelerateDecelerateInterpolator
        if (infinite) anim.repeatCount = Animation.INFINITE
        anim.duration = 2500
        return anim
    }

    fun bumped(duration: Long = 500): Animation {
        val anim = ScaleAnimation(0f, 1f, 0f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, .5f,
                ScaleAnimation.RELATIVE_TO_SELF, .5f)
        anim.interpolator = overshootInterpolator
        anim.duration = duration
        return anim
    }

    fun vanish(duration: Long = 500): Animation {
        val anim = ScaleAnimation(
                0f, 1f, 0f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, .0f,
                ScaleAnimation.RELATIVE_TO_SELF, .0f
        )
        anim.interpolator = overshootInterpolator
        anim.duration = duration
        return anim
    }

}
