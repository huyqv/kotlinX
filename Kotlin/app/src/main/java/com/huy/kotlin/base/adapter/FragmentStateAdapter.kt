package com.huy.kotlin.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/7/4
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class FragmentStateAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    interface InitFragmentChanged {
        fun onInitFragmentChanged(obj: Any): Boolean
    }

    private var fragments = ArrayList<Fragment>()

    var selectedPosition: Int = 0

    var initFragmentChanged: InitFragmentChanged? = null

    var currentFragment: Fragment? = null
        get() = if (fragments.isEmpty()) null else getItem(selectedPosition)

    fun add(fragment: Fragment): FragmentStateAdapter {
        fragments.add(fragment)
        return this
    }

    fun set(position: Int, fragment: Fragment) {
        fragments.removeAt(position)
        fragments.add(position, fragment)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItemPosition(`object`: Any): Int {
        if (null == initFragmentChanged)
            return POSITION_UNCHANGED
        if (initFragmentChanged!!.onInitFragmentChanged(`object`))
            return POSITION_NONE
        return POSITION_UNCHANGED
    }

    companion object {
        fun builder(fm: FragmentManager): FragmentStateAdapter {
            return FragmentStateAdapter(fm)
        }
    }

}