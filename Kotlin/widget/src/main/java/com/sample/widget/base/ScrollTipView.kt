package com.sample.widget.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.NestedScrollView
import com.sample.widget.extension.isShow

class ScrollTipView(context: Context, attrs: AttributeSet? = null) :
        AppCompatImageView(context, attrs) {

    fun updateScrollTipView(scrollView: NestedScrollView) {
        isShow(scrollView.hasInvisibleScrollContent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener { _, _, _, _, _ ->
                isShow(scrollView.hasInvisibleScrollContent)
            }
        } else {
            scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
                isShow(scrollView.hasInvisibleScrollContent)
            })
        }
    }

    private val NestedScrollView.hasInvisibleScrollContent: Boolean
        @SuppressLint("RestrictedApi")
        get() {
            return this.scrollY < (this.computeVerticalScrollRange() - this.height)
        }
}