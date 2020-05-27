package com.huy.library.adapter.fragment

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/08/06
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class InfinityPagerAdapter<T> : ViewPagerAdapter<T>() {

    override fun get(position: Int): T? {
        if (data.isEmpty()) return null
        return data[position % size]
    }

    override fun getCount(): Int {
        return data.size * 10000000
    }
}