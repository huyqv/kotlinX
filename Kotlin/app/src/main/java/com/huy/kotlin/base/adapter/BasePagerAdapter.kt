package com.huy.kotlin.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewpager.widget.PagerAdapter

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/08/06
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BasePagerAdapter<T> : PagerAdapter() {

    var selectedItem: T? = null

    var data: MutableList<T> = mutableListOf()

    val size: Int get() = data.size

    fun set(collection: List<T>?) {
        collection ?: return
        data.addAll(collection)
        notifyDataSetChanged()
    }

    fun set(element: T?) {
        element ?: return
        data.add(element)
        notifyDataSetChanged()
    }

    fun get(position: Int): T? {
        if (data.isEmpty())
            return null
        if (position !in 0 until data.size)
            return null
        return data[position]
    }

    fun indexOf(element: T?): Int {
        element ?: return -1
        return data.indexOf(element)
    }

    fun clear() {
        data = mutableListOf()
        notifyDataSetChanged()
    }

    fun selectedPosition(): Int {
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
        return if (position >= 0) position else PagerAdapter.POSITION_NONE
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

}