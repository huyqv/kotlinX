package com.sample.library.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Base fragment adapter for [androidx.viewpager2.widget.ViewPager2]
 */
open class FragmentAdapter : FragmentStateAdapter {

    /**
     * [FragmentStateAdapter] override
     */
    constructor(fragment: Fragment)
            : super(fragment.childFragmentManager, fragment.lifecycle)

    constructor(activity: FragmentActivity)
            : super(activity.supportFragmentManager, activity.lifecycle)

    constructor(fragmentManager: FragmentManager, lifecycleOwner: LifecycleOwner)
            : super(fragmentManager, lifecycleOwner.lifecycle)

    override fun createFragment(position: Int): Fragment {
        val fragment = fragments[position]
        fragment.arguments = Bundle().apply {
            putInt("position", position + 1)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    /**
     * [FragmentAdapter] properties
     */
    open var fragments = mutableListOf<Fragment>()

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