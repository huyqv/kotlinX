package com.kotlin.app.ui.home


import android.view.LayoutInflater
import com.kotlin.app.databinding.HomeBinding
import com.kotlin.app.ui.base.BaseFragment


class HomeFragment : BaseFragment<HomeBinding>() {

    override fun inflating(): (LayoutInflater) -> HomeBinding {
        return HomeBinding::inflate
    }

    override fun onViewCreated() {

    }

    override fun onLiveDataObserve() {
    }

}