package com.kotlin.app.ui.splash

import android.view.LayoutInflater
import com.kotlin.app.R
import com.kotlin.app.databinding.SplashBinding
import com.kotlin.app.ui.base.BaseFragment
import com.sample.widget.extension.post

class SplashFragment : BaseFragment<SplashBinding>() {

    override fun inflating(): (LayoutInflater) -> SplashBinding {
        return SplashBinding::inflate
    }

    override fun onViewCreated() {
        view.post(1200) {
            navigate(R.id.action_global_introFragment)
        }
    }

    override fun onLiveDataObserve() {
    }

}