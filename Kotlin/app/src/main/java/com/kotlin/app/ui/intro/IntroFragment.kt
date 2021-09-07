package com.kotlin.app.ui.intro

import android.view.LayoutInflater
import com.kotlin.app.R
import com.kotlin.app.databinding.ChatBinding
import com.kotlin.app.databinding.IntroBinding
import com.kotlin.app.ui.main.MainFragment

class IntroFragment : MainFragment<IntroBinding>() {

    override fun inflating(): (LayoutInflater) -> IntroBinding {
        return IntroBinding::inflate
    }

    override fun onViewCreated() {
        bind.textView.setOnClickListener {
            navigate(R.id.action_global_homeFragment) {
                setLaunchSingleTop()
            }
        }
    }

    override fun onLiveDataObserve() {
    }

}