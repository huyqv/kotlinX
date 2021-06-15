package com.kotlin.app.ui.splash

import com.example.library.extension.post
import com.kotlin.app.MainDirections
import com.kotlin.app.R
import kotlinx.android.synthetic.main.splash.*
import template.ui.BaseFragment

class SplashFragment : BaseFragment() {

    override fun layoutResource(): Int {
        return R.layout.splash
    }

    override fun onViewCreated() {
        textView.setOnClickListener {
            post(1000) {
                navigate(MainDirections.actionGlobalIntroFragment())
            }
        }
    }

    override fun onLiveDataObserve() {
    }

}