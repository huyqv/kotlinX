package com.kotlin.app.ui.splash

import android.view.LayoutInflater
import com.kotlin.app.R
import com.kotlin.app.databinding.SplashBinding
import com.kotlin.app.ui.base.BaseFragment
import com.sample.library.extension.launch

class SplashFragment : BaseFragment<SplashBinding>() {

    override fun inflating(): (LayoutInflater) -> SplashBinding {
        return SplashBinding::inflate
    }

    override fun onViewCreated() {
        launch(1200) {
            navigate(R.id.action_global_introFragment) {
                setLaunchSingleTop()
            }
        }
    }

    override fun onLiveDataObserve() {
    }

}