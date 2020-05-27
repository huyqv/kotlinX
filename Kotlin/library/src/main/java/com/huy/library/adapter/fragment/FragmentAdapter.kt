package com.huy.library.adapter.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class FragmentAdapter : FragmentPagerAdapter {

    private var fragments = mutableListOf<Fragment>()

    constructor(fm: FragmentManager, vararg fragments: Fragment) : super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        this.fragments.addAll(fragments)
    }

    fun add(fragment: Fragment): FragmentAdapter {
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

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int): CharSequence? = null

}