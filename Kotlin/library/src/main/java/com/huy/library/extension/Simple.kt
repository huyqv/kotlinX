package com.huy.library.extension

import android.animation.Animator
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager.widget.ViewPager

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/03/12
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface SimpleAnimationListener : Animation.AnimationListener {
    override fun onAnimationRepeat(animation: Animation?) {
    }

    override fun onAnimationEnd(animation: Animation?) {
    }

    override fun onAnimationStart(animation: Animation?) {
    }
}

interface SimpleAnimatorListener : Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationStart(animation: Animator?) {
    }


}

interface SimpleTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }
}

interface SimpleDrawerListener : DrawerLayout.DrawerListener {
    override fun onDrawerStateChanged(p0: Int) {
    }

    override fun onDrawerSlide(p0: View, p1: Float) {
    }

    override fun onDrawerClosed(p0: View) {
    }

    override fun onDrawerOpened(p0: View) {
    }
}

abstract class SimpleDiffCallback<T>(private val oldList: Collection<T>, private val newList: Collection<T>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return try {
            areItemSame(oldList.elementAt(oldItemPosition), newList.elementAt(newItemPosition))
        } catch (e: IndexOutOfBoundsException) {
            false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return try {
            areContentSame(oldList.elementAt(oldItemPosition), newList.elementAt(newItemPosition))
        } catch (e: IndexOutOfBoundsException) {
            false
        }
    }

    abstract fun areItemSame(old: T, new: T): Boolean

    open fun areContentSame(old: T, new: T): Boolean {
        return areItemSame(old, new)
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