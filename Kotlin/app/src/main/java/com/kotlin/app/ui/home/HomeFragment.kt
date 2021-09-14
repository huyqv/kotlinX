package com.kotlin.app.ui.home


import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.kotlin.app.databinding.HomeBinding
import com.kotlin.app.ui.base.BaseFragment


class HomeFragment : BaseFragment<HomeBinding>() {

    override fun inflating(): (LayoutInflater) -> ViewBinding {
        return HomeBinding::inflate
    }

    override fun onViewCreated() {
        bind.textView.setOnClickListener {
            bind.inputView1.error = "Error text"
            bind.inputView2.error = "Error text"
        }
    }

    override fun onLiveDataObserve() {
    }

}