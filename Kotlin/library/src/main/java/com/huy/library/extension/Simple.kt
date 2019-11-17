package com.huy.library.extension

import android.animation.Animator
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/03/12
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SimpleAnimationListener : Animation.AnimationListener {
    override fun onAnimationRepeat(animation: Animation?) {
    }

    override fun onAnimationEnd(animation: Animation?) {
    }

    override fun onAnimationStart(animation: Animation?) {
    }
}

class SimpleAnimatorListener : Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationStart(animation: Animator?) {
    }


}

class SimpleTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }
}

class ViewSimpleListener : DrawerLayout.DrawerListener {
    override fun onDrawerStateChanged(p0: Int) {
    }

    override fun onDrawerSlide(p0: View, p1: Float) {
    }

    override fun onDrawerClosed(p0: View) {
    }

    override fun onDrawerOpened(p0: View) {
    }
}

interface SimplePageChangeListener : ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
    }
}