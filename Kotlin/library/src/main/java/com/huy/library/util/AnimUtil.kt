package com.huy.library.util

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.animation.*
import androidx.annotation.AnimRes

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/11/06
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object AnimUtil {

    private val overshootInterpolator by lazy { OvershootInterpolator() }

    private val accelerateDecelerateInterpolator by lazy { AccelerateDecelerateInterpolator() }

    fun get(context: Context, @AnimRes animRes: Int, duration: Long, fillAfter: Boolean = true): Animation {
        val anim = AnimationUtils.loadAnimation(context, animRes)
        anim.duration = duration
        anim.fillAfter = fillAfter
        return anim
    }

    fun animate(view: View, @AnimRes animRes: Int, duration: Long, fillAfter: Boolean = true) {
        view.startAnimation(get(view.context, animRes, duration, fillAfter))
    }

    fun hide(view: View, @AnimRes animRes: Int, duration: Long, fillAfter: Boolean = true) {
        val anim = get(view.context, animRes, duration, fillAfter)
        anim.hideOnEnd(view)
        view.startAnimation(anim)
    }

    fun show(view: View, @AnimRes animRes: Int, duration: Long, fillAfter: Boolean = true) {
        view.visibility = View.VISIBLE
        val anim = get(view.context, animRes, duration, fillAfter)
        view.startAnimation(anim)
    }

    fun translateHorizontal(fromX: Int, toX: Int, duration: Long, fillAfter: Boolean = true): Animation {
        val anim = TranslateAnimation(fromX.toFloat(), toX.toFloat(), 0f, 0f)
        anim.duration = duration
        anim.fillAfter = fillAfter
        return anim
    }

    fun translateVertically(fromY: Int, toY: Int, duration: Long, fillAfter: Boolean = true): Animation {
        val anim = TranslateAnimation(0f, 500f, fromY.toFloat(), toY.toFloat())
        anim.duration = duration
        anim.fillAfter = fillAfter
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

    fun scaleLeft(duration: Long = 500, fillAfter: Boolean = true): Animation {
        val anim = ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 0f)
        anim.duration = duration
        anim.fillAfter = fillAfter
        return anim
    }

    fun scaleRight(duration: Long = 500, fillAfter: Boolean = true): Animation {
        val anim = ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 1f)
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

    fun rotateForever(): Animation {
        val anim = RotateAnimation(0f, 36000f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.repeatCount = Animation.INFINITE
        anim.duration = 70000
        return anim
    }

    fun rotate(toDegrees: Float = 360f, duration: Long = 1000, infinite: Boolean): Animation {
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

    fun Animation.hideOnEnd(view: View?) {
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                view?.visibility = View.INVISIBLE
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
    }

}
