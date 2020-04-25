package com.huy.library.extension

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.*
import androidx.annotation.AnimRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/02/24
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
private const val DURATION = 3000L

private val overshootInterpolator: OvershootInterpolator get() = OvershootInterpolator()

private val accelerateDecelerateInterpolator: AccelerateDecelerateInterpolator = AccelerateDecelerateInterpolator()

fun View.animate(@AnimRes animRes: Int, duration: Long = 2000, fillAfter: Boolean = true) {

    val anim = AnimationUtils.loadAnimation(context, animRes)
    anim.duration = duration
    anim.fillAfter = fillAfter
    startAnimation(anim)
}

fun View.animate(@AnimRes animRes: Int, duration: Long, block: () -> Unit) {
    val anim = AnimationUtils.loadAnimation(context, animRes)
    anim.duration = duration
    anim.fillAfter = false
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(animation: Animation?) {
            block()
        }

        override fun onAnimationStart(animation: Animation?) {
        }

        override fun onAnimationRepeat(animation: Animation?) {
        }

    })
    startAnimation(anim)
}

fun View.animateHide(@AnimRes animRes: Int, duration: Long = 2000, fillAfter: Boolean = true) {

    val anim = AnimationUtils.loadAnimation(context, animRes)
    anim.duration = duration
    anim.fillAfter = fillAfter
    anim.onAnimationEnd { visibility = View.INVISIBLE }
    startAnimation(anim)
}

fun View.animateShow(@AnimRes animRes: Int, duration: Long = 2000, fillAfter: Boolean = true) {
    visibility = View.VISIBLE
    val anim = AnimationUtils.loadAnimation(context, animRes)
    anim.duration = duration
    anim.fillAfter = fillAfter
    startAnimation(anim)
}

fun View.colorAnimate(@ColorRes fromColor: Int, @ColorRes toColor: Int): ObjectAnimator {

    val objectAnimator = ObjectAnimator.ofObject(
            this, "backgroundColor", ArgbEvaluator(),
            ContextCompat.getColor(context, fromColor),
            ContextCompat.getColor(context, toColor)
    )
    objectAnimator.repeatCount = 1
    objectAnimator.repeatMode = ValueAnimator.REVERSE
    objectAnimator.duration = DURATION
    return objectAnimator
}

fun animTranslateX(from: Float, to: Float): Animation {
    return TranslateAnimation(from, to, 0f, 0f).apply {
        duration = DURATION
    }
}

fun animTranslateY(from: Float, to: Float): Animation {
    return TranslateAnimation(0f, 0f, from, to).apply {
        duration = DURATION
    }
}

fun animFadeIn(): Animation {
    return AlphaAnimation(0f, 1f).apply {
        duration = DURATION
    }
}

fun animFadeOut(): Animation {
    return AlphaAnimation(1f, 0f).apply {
        duration = DURATION
    }
}

fun animCenterScale(): Animation {
    return ScaleAnimation(
            0f, 1f, 0f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
    ).apply {
        duration = DURATION
    }
}

fun animLeftScale(): Animation {
    return ScaleAnimation(
            0f, 1f, 0f, 1f,
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f
    ).apply {
        duration = DURATION
    }
}

fun animRightScale(): Animation {
    return ScaleAnimation(
            0f, 1f, 0f, 1f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0.5f
    ).apply {
        duration = DURATION
    }
}

fun animBumped(): Animation {
    return ScaleAnimation(0f, 1f, 0f, 1f,
            ScaleAnimation.RELATIVE_TO_SELF, .5f,
            ScaleAnimation.RELATIVE_TO_SELF, .5f).apply {
        duration = DURATION
        interpolator = OvershootInterpolator()
    }
}

fun animVanish(): Animation {
    return ScaleAnimation(0f, 1f, 0f, 1f,
            ScaleAnimation.RELATIVE_TO_SELF, .0f,
            ScaleAnimation.RELATIVE_TO_SELF, .0f).apply {
        duration = DURATION
        interpolator = OvershootInterpolator()
    }
}

fun animRotate(v: View): ObjectAnimator {
    return ObjectAnimator.ofFloat(v, "rotation", 0f, 360f).apply {
        duration = DURATION
        interpolator = DecelerateInterpolator()
        repeatCount = ObjectAnimator.INFINITE
    }
}

fun animRotateAxisX(v: View): ObjectAnimator {
    return ObjectAnimator.ofFloat(v, "rotationX", 0.0f, 360f).apply {
        duration = DURATION
        interpolator = AccelerateDecelerateInterpolator()
        repeatCount = ObjectAnimator.INFINITE
    }
}

fun animRotateAxisY(v: View): ObjectAnimator {
    return ObjectAnimator.ofFloat(v, "rotationY", 0.0f, 360f).apply {
        duration = DURATION
        interpolator = AccelerateDecelerateInterpolator()
        repeatCount = ObjectAnimator.INFINITE
    }
}

fun Animation?.onAnimationStart(onStart: () -> Unit): Animation? {
    this?.setAnimationListener(object : SimpleAnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            onStart()
        }
    })
    return this
}

fun Animation?.onAnimationEnd(onEnd: () -> Unit): Animation? {
    this?.setAnimationListener(object : SimpleAnimationListener {
        override fun onAnimationEnd(animation: Animation?) {
            onEnd()
        }
    })
    return this
}

fun ObjectAnimator?.onAnimatorStart(onStart: () -> Unit): ObjectAnimator? {
    this?.addListener(object : SimpleAnimatorListener {
        override fun onAnimationStart(animator: Animator?) {
            onStart()
        }
    })
    return this
}

fun ObjectAnimator?.onAnimatorEnd(onEnd: () -> Unit): ObjectAnimator? {
    this?.addListener(object : SimpleAnimatorListener {
        override fun onAnimationEnd(animator: Animator?) {
            onEnd()
        }
    })
    return this
}

interface SimpleAnimationListener : Animation.AnimationListener {
    override fun onAnimationRepeat(animation: Animation?) {
    }

    override fun onAnimationEnd(animation: Animation?) {
    }

    override fun onAnimationStart(animation: Animation?) {
    }
}

interface SimpleAnimatorListener : Animator.AnimatorListener {
    override fun onAnimationRepeat(animator: Animator?) {
    }

    override fun onAnimationEnd(animator: Animator?) {
    }

    override fun onAnimationCancel(animator: Animator?) {
    }

    override fun onAnimationStart(animator: Animator?) {
    }
}
