package com.kotlin.app.ui.intro

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.kotlin.app.R
import com.kotlin.app.databinding.IntroBinding
import com.kotlin.app.ui.main.MainFragment

class IntroFragment : MainFragment<IntroBinding>() {

    override fun inflating(): (LayoutInflater) -> ViewBinding {
        return IntroBinding::inflate
    }

    override fun onViewCreated() {
        bind.textView.setOnClickListener {
            navigate(R.id.action_global_homeFragment) {
                clearBackStack()
            }
        }
    }

    override fun onLiveDataObserve() {
    }

}