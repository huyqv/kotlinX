package com.huy.library.view

import androidx.viewpager.widget.ViewPager

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/07/05
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface SimplePageChangeListener : ViewPager.OnPageChangeListener {

    override fun onPageScrollStateChanged(state: Int) {
        when (state) {
            ViewPager.SCROLL_AXIS_VERTICAL -> {
            }
            ViewPager.SCROLL_AXIS_HORIZONTAL -> {
            }
            ViewPager.SCROLL_AXIS_NONE -> {
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

    }
}