package com.example.library.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class FragmentPagerAdapter(fm: FragmentManager)
    : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    /**
     * [FragmentStatePagerAdapter] override
     */
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }


    /**
     * [FragmentPagerAdapter] properties
     */
    private var fragments = mutableListOf<Fragment>()

    fun set(position: Int, fragment: Fragment) {
        fragments.removeAt(position)
        fragments.add(position, fragment)
        notifyDataSetChanged()
    }

    fun addFragments(vararg frags: Fragment) {
        fragments.addAll(frags)
        notifyDataSetChanged()
    }

}