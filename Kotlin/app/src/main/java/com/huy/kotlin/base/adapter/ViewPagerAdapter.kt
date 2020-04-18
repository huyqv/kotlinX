package com.huy.kotlin.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/08/06
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class ViewPagerAdapter<T> : PagerAdapter() {

    var selectedItem: T? = null

    var data: MutableList<T> = mutableListOf()

    val size: Int get() = data.size

    open fun set(collection: List<T>?) {
        collection ?: return
        data.addAll(collection)
        notifyDataSetChanged()
    }

    open fun set(element: T?) {
        element ?: return
        data.add(element)
        notifyDataSetChanged()
    }

    open fun get(position: Int): T? {
        if (data.isEmpty())
            return null
        if (position !in 0 until data.size)
            return null
        return data[position]
    }

    open fun indexOf(element: T?): Int {
        element ?: return -1
        return data.indexOf(element)
    }

    open fun clear() {
        data = mutableListOf()
        notifyDataSetChanged()
    }

    open fun selectedPosition(): Int {
        selectedItem ?: return -1
        return indexOf(selectedItem)
    }

    @LayoutRes
    abstract fun layoutRes(): Int

    abstract fun View.onBind(model: T)

    override fun getCount(): Int {
        return size
    }

    override fun getItemPosition(obj: Any): Int {
        @Suppress("UNCHECKED_CAST")
        val position = data.indexOf(obj as T)
        return if (position >= 0) position else POSITION_NONE
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return obj == view
    }

    override fun instantiateItem(viewGroup: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(viewGroup.context).inflate(layoutRes(), viewGroup, false)
        view.onBind(data[position])
        viewGroup.addView(view, 0)
        return view
    }

    override fun destroyItem(viewGroup: ViewGroup, position: Int, obj: Any) {
        viewGroup.removeView(obj as View)
    }

    class FadeZoom : ViewPager.PageTransformer {

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
                    view.alpha = MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)

                }
                // (1,+Infinity] This page is way off-screen to the right.
                else -> view.alpha = 0f
            }
        }


    }

    class Parallax(private val dummyView: View?) : ViewPager.PageTransformer {

        override fun transformPage(view: View, position: Float) {

            val pageWidth = view.width

            when {

                // [-Infinity,-1) This page is way off-screen to the left.
                position < -1 -> view.alpha = 1f

                // [-1,1]
                position <= 1 -> dummyView?.translationX = -position * (pageWidth / 2) //Half the normal speed

                // (1,+Infinity] This page is way off-screen to the right.
                else -> view.alpha = 1f
            }


        }
    }

    class Stack : ViewPager.PageTransformer {

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

    class Slide : ViewPager.PageTransformer {

        override fun transformPage(view: View, position: Float) {

            view.translationX = view.width * -position

            if (position > 0) {
                view.translationY = position * view.height
            }

        }
    }

    class Slide2 : ViewPager.PageTransformer {

        override fun transformPage(view: View, position: Float) {

            view.translationX = view.width * -position

            if (position > 0) {
                view.translationY = position * view.height
            } else {
                view.translationY = position * -view.height
            }

        }
    }

    class Fade : ViewPager.PageTransformer {
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

}