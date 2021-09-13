package com.sample.library.fragment

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class PageTransformer {

    class Parallax(val parallaxViewId: () -> Int) : ViewPager2.PageTransformer {

        override fun transformPage(view: View, position: Float) {
            when {
                // [-Infinity,-1) This page is way off-screen to the left.
                position < -1 -> {
                    view.alpha = 1f
                }
                // [-1,1] Half the normal speed
                position <= 1 -> {
                    view.findViewById<View>(parallaxViewId())?.translationX =
                        -position * (view.width / 2)
                }
                // (1,+Infinity] This page is way off-screen to the right.
                else -> {
                    view.alpha = 1f
                }
            }
        }
    }

    class FadeZoom : ViewPager2.PageTransformer {

        companion object {
            private const val MIN_SCALE = 0.85f
            private const val MIN_ALPHA = 0.5f
        }

        override fun transformPage(view: View, position: Float) {

            val pageWidth = view.width
            val pageHeight = view.height

            when {

                // [-Infinity,-1) This page is way off-screen to the left.
                position < -1 -> view.alpha = 0f

                // Modify the default slide transition to shrink the page as well
                position <= 1 -> { // [-1,1]
                    val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                    val verticalMargin = pageHeight * (1 - scaleFactor) / 2
                    val horizontalMargin = pageWidth * (1 - scaleFactor) / 2
                    if (position < 0) {
                        view.translationX = horizontalMargin - verticalMargin / 2
                    } else {
                        view.translationX = -horizontalMargin + verticalMargin / 2
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    view.scaleX = scaleFactor
                    view.scaleY = scaleFactor

                    // Fade the page relative to its size.
                    view.alpha =
                        MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
                }
                // (1,+Infinity] This page is way off-screen to the right.
                else -> view.alpha = 0f
            }
        }
    }

    class Stack : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {

            val pageWidth = view.width

            val pageHeight = view.height

            if (-1 < position && position < 0) {

                val scaleFactor = 1 - Math.abs(position) * 0.1f
                val verticalMargin = pageHeight * (1 - scaleFactor) / 2
                val horizontalMargin = pageWidth * (1 - scaleFactor) / 2

                if (position < 0) {
                    view.translationX = horizontalMargin - verticalMargin / 2
                } else {
                    view.translationX = -horizontalMargin + verticalMargin / 2
                }

                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
            }

            view.translationX = view.width * -position

            if (position > 0) {
                view.translationY = position * view.height
            }
        }
    }

    class VerticalSlide : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            when {
                position < -1 -> {
                    view.alpha = 0f
                }
                position <= 1 -> {
                    view.alpha = 1f
                    view.translationX = view.width * -position
                    val yPosition = position * view.height
                    view.translationY = yPosition
                }
                else -> {
                    view.alpha = 0f
                }
            }
        }
    }

    class ReservedVerticalSlide : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {

            view.translationX = view.width * -position

            if (position > 0) {
                view.translationY = position * view.height
            }
        }
    }

    class HorizontalSlide : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {

            view.translationX = view.width * -position

            if (position > 0) {
                view.translationY = position * view.height
            } else {
                view.translationY = position * -view.height
            }
        }
    }

    class Fade : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            if (position <= -1.0F || position >= 1.0F) {
                page.alpha = 0.0F
            } else if (position == 0.0F) {
                page.alpha = 1.0F
            } else {
                page.alpha = 1.0F - abs(position)
            }
        }
    }

    class None : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            when {
                position < 0 -> view.scrollX = (view.width.toFloat() * position).toInt()
                position > 0 -> view.scrollX = (-(view.width.toFloat() * -position)).toInt()
                else -> view.scrollX = 0
            }
        }
    }
}