package com.kotlin.app.ui.splash

import android.graphics.Color
import android.view.LayoutInflater
import com.kotlin.app.R
import com.kotlin.app.databinding.SplashBinding
import com.kotlin.app.ui.main.MainFragment
import com.sample.library.extension.*

class SplashFragment : MainFragment<SplashBinding>() {

    override fun inflating(): (LayoutInflater) -> SplashBinding {
        return SplashBinding::inflate
    }

    override fun onViewCreated() {
        launch(2400) {
            mainActivity?.setBackground(Color.WHITE)
            statusBarColor(Color.WHITE)
            darkSystemWidgets()
            navigate(R.id.action_global_introFragment) {
                setLaunchSingleTop()
            }
        }
    }

    override fun onLiveDataObserve() {
    }

}