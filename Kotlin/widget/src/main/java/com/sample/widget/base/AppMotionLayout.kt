package com.sample.widget.base

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.IdRes
import androidx.constraintlayout.motion.widget.MotionLayout
import com.sample.widget.extension.SimpleMotionTransitionListener
import kotlin.math.pow
import kotlin.math.sqrt

open class AppMotionLayout : MotionLayout {

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        addTransitionListener(object : SimpleMotionTransitionListener {
            override fun onTransitionCompleted(layout: MotionLayout, currentId: Int) {
                this@AppMotionLayout.also {
                    it.isInteractionEnabled = true
                    it.previousId = it.currentId
                    it.currentId = currentId
                }
            }
        })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(enableInteraction)
    }

    var previousId: Int = -1

    var currentId: Int = -1

    private var touchView: View? = null

    var onTouchUp: (View?) -> Unit = {}

    var onClickEvent: (View) -> Unit = {}

    var onEvent = false

    private var touchX: Float = -1f

    private var touchY: Float = -1f

    private var touchTime: Long = 0

    private val clickableViews = mutableListOf<View>()

    private val isTransitionCompleted: Boolean get() = progress == 0f || progress == 1f

    private val enableInteraction = Runnable {
        isInteractionEnabled = true
    }

    open fun onViewClick(v: View) {
        onClickEvent(v)
    }

    fun addViewClickListener(vararg views: View) {
        views.forEach {
            it.isClickable = false
            clickableViews.add(it)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if ((progress != 0f && progress != 1f) && !onEvent) {
            touchView = null
            return super.onTouchEvent(event)
        }
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = event.x
                touchY = event.y
                touchTime = System.currentTimeMillis()
                touchView = getViewOnTouchEvent(event)
            }
            MotionEvent.ACTION_UP -> {
                detectActionClick(event)
            }
        }
        return super.onTouchEvent(event)
    }

    private fun detectActionClick(event: MotionEvent) {
        val v = getViewOnTouchEvent(event)
        if (isTransitionCompleted && touchView != null && touchView == v) {
            val touchDistance = sqrt((event.x - touchX).pow(2F) + (event.y - touchY).pow(2.0f))
            val touchDuration = System.currentTimeMillis() - touchTime
            if (touchDistance < 6F && touchDuration < 250) {
                onViewClick(touchView!!)
            }
        }
        onTouchUp(v)
        touchView = null
        touchX = -1f
        touchY = -1f
        touchTime = -1
    }

    private fun getViewOnTouchEvent(event: MotionEvent): View? {
        val x = event.x.toInt()
        val y = event.y.toInt()
        clickableViews.forEach {
            val scaleWidth = (it.width - it.width * it.scaleX).toInt() / 2
            val scaleHeight = (it.height - it.height * it.scaleY).toInt() / 2
            if (
                    x in it.left + scaleWidth..it.right - scaleWidth &&
                    y in it.top + scaleHeight..it.bottom - scaleHeight
            ) {
                return it
            }
        }
        return null
    }

    fun safeTransitionTo(@IdRes transitionId: Int) {
        if (progress == 0f && progress == 1f) return
        this.post {
            isInteractionEnabled = false
            super.transitionToState(transitionId)
        }
        this.postDelayed(enableInteraction, 320)
    }
}

