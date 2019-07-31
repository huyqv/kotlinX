package com.huy.kotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ViewFlipper
import com.google.android.material.tabs.TabLayout
import com.huy.library.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/08/29
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SliderView : ViewFlipper {

    interface ViewChangedListener {

        fun onViewChangedListener(v: View?, index: Int)
    }

    var next = true

    var viewChangeListener: ViewChangedListener? = null

    private var whichChild = 0

    private val nextPerformAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.enter)

    private val nextExitAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.exit)

    private val backPerformAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.pop_enter)

    private val backExitAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.pop_exit)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun showNext() {

        next = true
        displayedChild = whichChild + 1
    }

    override fun showPrevious() {

        next = false
        displayedChild = whichChild - 1
    }

    override fun setDisplayedChild(whichChild: Int) {

        this.whichChild = whichChild

        if (whichChild >= childCount) this.whichChild = 0
        else if (whichChild < 0) this.whichChild = childCount - 1

        val hasFocus = focusedChild != null

        if (next) next(whichChild)
        else previous(whichChild)

        if (hasFocus) requestFocus(View.FOCUS_FORWARD)

        viewChangeListener?.onViewChangedListener(this.currentView, this.whichChild)
    }

    private fun next(childIndex: Int) {

        val count = childCount
        for (i in 0 until count) {

            val child = getChildAt(i)

            if (i == childIndex) {

                child.startAnimation(nextPerformAnimation)
                child.visibility = View.VISIBLE
            } else {

                if (child.visibility == View.VISIBLE) child.startAnimation(nextExitAnimation)
                else if (child.animation === nextPerformAnimation) child.clearAnimation()
                child.visibility = View.GONE
            }
        }
    }

    private fun previous(childIndex: Int) {

        val count = childCount
        for (i in 0 until count) {

            val child = getChildAt(i)

            if (i == childIndex) {
                child.startAnimation(backPerformAnimation)
                child.visibility = View.VISIBLE
            } else {

                if (child.visibility == View.VISIBLE) child.startAnimation(backExitAnimation)
                else if (child.animation === backPerformAnimation) child.clearAnimation()
                child.visibility = View.GONE
            }
        }
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)

        child.visibility = if (childCount == 1) View.VISIBLE else View.GONE

        if (index in 0..whichChild) displayedChild = whichChild + 1
    }

    override fun removeAllViews() {
        super.removeAllViews()
        whichChild = 0
    }

    override fun removeViewAt(index: Int) {
        super.removeViewAt(index)

        when {
            childCount == 0 -> whichChild = 0
            whichChild >= childCount -> displayedChild = childCount - 1
            whichChild == index -> displayedChild = whichChild
        }
    }

    override fun removeViews(start: Int, count: Int) {
        super.removeViews(start, count)

        if (childCount == 0) whichChild = 0
        else if (whichChild >= start && whichChild < start + count) displayedChild = whichChild
    }

    override fun getCurrentView(): View? {
        return getChildAt(whichChild)
    }

    fun setUpWithSlidingView(tabLayout: TabLayout, viewChangedListener: ViewChangedListener? = null) {

        for (i in 0 until childCount) tabLayout.addTab(tabLayout.newTab())

        tabLayout.getTabAt(0)?.select()
        viewChangeListener = object : ViewChangedListener {
            override fun onViewChangedListener(v: View?, index: Int) {

                tabLayout.getTabAt(index)?.select()
                viewChangedListener?.onViewChangedListener(v, index)
            }
        }
    }

}