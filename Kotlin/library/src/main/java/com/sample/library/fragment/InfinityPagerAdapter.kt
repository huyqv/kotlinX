package com.sample.library.fragment

abstract class InfinityPagerAdapter<T> : ViewPagerAdapter<T>() {

    override fun get(position: Int): T? {
        if (data.isEmpty()) return null
        return data[position % size]
    }

    override fun getCount(): Int {
        return data.size * 10000000
    }
}