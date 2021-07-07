package com.kotlin.app.ui.intro

import android.view.LayoutInflater
import com.kotlin.app.R
import com.kotlin.app.databinding.IntroBinding
import com.kotlin.app.ui.main.MainFragment
import com.sample.widget.extension.post

class IntroFragment : MainFragment<IntroBinding>() {

    override fun inflating(): (LayoutInflater) -> IntroBinding {
        return IntroBinding::inflate
    }

    override fun onViewCreated() {
        bind.textView.setOnClickListener {
            view.post(1200) {
                navigate(R.id.action_global_homeFragment)
            }
        }
    }

    override fun onLiveDataObserve() {
    }

}