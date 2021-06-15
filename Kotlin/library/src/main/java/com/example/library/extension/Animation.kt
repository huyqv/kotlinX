package com.example.library.extension

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.*
import androidx.annotation.AnimRes
import androidx.annotation.ColorRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager

val interpolator = DecelerateInterpolator()
val interpolator1 = AccelerateDecelerateInterpolator()
val interpolator2 = OvershootInterpolator()
val interpolator3 = CycleInterpolator(1f)
val interpolator4 = OvershootInterpolator()
val interpolator5 = AnticipateInterpolator()
val interpolator6 = AnticipateOvershootInterpolator()
val interpolator7 = BounceInterpolator()
val interpolator8 = LinearInterpolator()


fun View.animateAlpha(from: Float, to: Float) {
    this.post {
        if (alpha != from) alpha = from
        val anim = AlphaAnimation(from, to)
        anim.duration = 600
        anim.onAnimationEnd {
            alpha = to
        }
        startAnimation(anim)
    }
}

fun View.animRotateAxisX(): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, "rotationX", 0.0F, 360F).apply {
        duration = 1000L
        repeatCount = ObjectAnimator.INFINITE
    }
}

fun View.animRotateAxisY(): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, "rotationY", 0.0F, 360F).apply {
        duration = 1000L
        repeatCount = ObjectAnimator.INFINITE
    }
}

fun View.animRotate(): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, "rotation", 0F, 360F).apply {
        duration = 1000L
        repeatCount = ObjectAnimator.INFINITE
    }
}


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
    objectAnimator.duration = 1000
    return objectAnimator
}

fun animTranslateX(from: Float, to: Float): TranslateAnimation {
    return TranslateAnimation(from, to, 0F, 0F).apply {
        duration = 1000L
    }
}

fun animTranslateY(from: Float, to: Float): TranslateAnimation {
    return TranslateAnimation(0F, 0F, from, to).apply {
        duration = 1000L
    }
}


fun ConstraintLayout.editConstraint(block: ConstraintSet.() -> Unit) {
    post {
        ConstraintSet().also {
            it.clone(this)
            it.block()
            it.applyTo(this)
        }
    }
}

fun Transition.beginTransition(layout: ConstraintLayout, block: ConstraintSet.() -> Unit): Transition {
    TransitionManager.beginDelayedTransition(layout, this)
    layout.editConstraint(block)
    return this
}

fun Transition.beginTransition(layout: ConstraintLayout, block: ConstraintSet.() -> Unit, onEnd: () -> Unit = {}): Transition {

    addListener(object : SimpleTransitionListener {
        override fun onTransitionEnd(transition: Transition) {
            transition.removeListener(this)
            onEnd()
        }
    })
    TransitionManager.beginDelayedTransition(layout, this)
    layout.editConstraint(block)
    return this
}

fun ConstraintLayout.beginTransition(duration: Long, block: ConstraintSet.() -> Unit) {
    ChangeBounds().also {
        it.duration = duration
        it.startDelay = 0
    }.beginTransition(this, block)
}

fun ConstraintLayout.beginTransition(duration: Long, block: ConstraintSet.() -> Unit, onEnd: () -> Unit = {}) {
    ChangeBounds().also {
        it.duration = duration
        it.startDelay = 0
    }.beginTransition(this, block, onEnd)

}

fun MotionLayout.transitionToState(transitionId: Int, onCompleted: () -> Unit) {
    addTransitionListener(object : SimpleMotionTransitionListener {
        override fun onTransitionCompleted(layout: MotionLayout?, currentId: Int) {
            removeTransitionListener(this)
            onCompleted()
        }
    })
    transitionToState(transitionId)

}

class ConstraintBuilder(private val constraintLayout: ConstraintLayout) {

    private val constraintSetList = mutableListOf<ConstraintSet.() -> Unit>()

    private val transitionList = mutableListOf<Transition>()

    fun transform(_duration: Long = 400, block: ConstraintSet.() -> Unit): ConstraintBuilder {
        transitionList.add(ChangeBounds().also {
            it.duration = _duration
        })
        constraintSetList.add(block)
        return this
    }

    fun start() {
        if (transitionList.isEmpty()) return
        for (i in 0..transitionList.lastIndex) {
            transitionList[i].addListener(object : SimpleTransitionListener {
                override fun onTransitionEnd(transition: Transition) {
                    transitionList[i].removeListener(this)
                    if (i < transitionList.lastIndex) {
                        transitionList[i + 1].beginTransition(constraintLayout, constraintSetList[i + 1])
                    }
                }
            })
        }
        transitionList[0].beginTransition(constraintLayout, constraintSetList[0])
    }

}


fun animCenterScale(): ScaleAnimation {
    return ScaleAnimation(
            0F, 1F, 0F, 1F,
            Animation.RELATIVE_TO_SELF, 0.5F,
            Animation.RELATIVE_TO_SELF, 0.5F
    ).apply {
        duration = 1000L
    }
}

fun animLeftScale(): ScaleAnimation {
    return ScaleAnimation(
            0F, 1f, 0F, 1f,
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0.5F
    ).apply {
        duration = 1000L
    }
}

fun animRightScale(): ScaleAnimation {
    return ScaleAnimation(
            0F, 1f, 0F, 1f,
            Animation.RELATIVE_TO_SELF, 0F,
            Animation.RELATIVE_TO_SELF, 0.5F
    ).apply {
        duration = 1000L
    }
}

fun animBumped(): ScaleAnimation {
    return ScaleAnimation(0F, 1f, 0F, 1f,
            ScaleAnimation.RELATIVE_TO_SELF, .5f,
            ScaleAnimation.RELATIVE_TO_SELF, .5f).apply {
        duration = 1000L

    }
}

fun animVanish(): ScaleAnimation {
    return ScaleAnimation(0F, 1f, 0F, 1f,
            ScaleAnimation.RELATIVE_TO_SELF, .0F,
            ScaleAnimation.RELATIVE_TO_SELF, .0F).apply {
        duration = 1000L
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

interface SimpleTransitionListener : Transition.TransitionListener {
    override fun onTransitionStart(transition: Transition) {
    }

    override fun onTransitionEnd(transition: Transition) {
    }

    override fun onTransitionCancel(transition: Transition) {
    }

    override fun onTransitionPause(transition: Transition) {
    }

    override fun onTransitionResume(transition: Transition) {
    }

}

