package com.sample.widget.extension

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager

interface SimpleTextWatcher : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }

    fun EditText.setTextSilently(s: String?) {
        removeTextChangedListener(this@SimpleTextWatcher)
        setText(s)
        setSelection(s?.length ?: 0)
        addTextChangedListener(this@SimpleTextWatcher)
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
