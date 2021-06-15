package com.example.library.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ViewFlipper
import com.example.library.R
import com.google.android.material.tabs.TabLayout

class SliderView : ViewFlipper {

    var next = true

    var onViewChanged: (View, Int) -> Unit = { _, _ -> }

    private var whichChild = 0

    private val nextPerformAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.horizontal_enter)

    private val nextExitAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.horizontal_exit)

    private val backPerformAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.horizontal_pop_enter)

    private val backExitAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.horizontal_pop_exit)

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    override fun showNext() {
        next = true
        displayedChild = whichChild + 1
    }

    override fun showPrevious() {
        next = false
        displayedChild = whichChild - 1
    }

    override fun setDisplayedChild(child: Int) {

        this.whichChild = child

        if (child >= childCount) this.whichChild = 0
        else if (child < 0) this.whichChild = childCount - 1

        val hasFocus = focusedChild != null

        if (next) next(child)
        else previous(child)

        if (hasFocus) requestFocus(View.FOCUS_FORWARD)

        currentView?.also {
            onViewChanged(it, whichChild)
        }

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

    fun setUpWithSlidingView(tabLayout: TabLayout, onViewChanged: (View, Int) -> Unit = { _, _ -> }) {

        for (i in 0 until childCount) tabLayout.addTab(tabLayout.newTab())

        tabLayout.getTabAt(0)?.select()
        this.onViewChanged = { view, index ->
            tabLayout.getTabAt(index)?.select()
            onViewChanged(view, index)
        }
    }

}