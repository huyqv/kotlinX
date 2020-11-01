package com.kotlin.app.ui.splash

import com.example.library.extension.addOnPressListener
import com.example.library.ui.BaseFragment
import com.kotlin.app.R
import kotlinx.android.synthetic.main.splash.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/11/01
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SplashFragment : BaseFragment() {

    override fun layoutResource(): Int {
        return R.layout.splash
    }

    override fun onViewCreated() {
        textViewSplash.addOnPressListener({

        }, {

        })
    }

    override fun onLiveDataObserve() {
    }

}