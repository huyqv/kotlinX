package com.kotlin.app.ui.splash

import com.example.library.extension.showKeyboard
import com.kotlin.app.R
import com.kotlin.app.ui.base.BaseFragment

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
        showKeyboard()
    }

    override fun onLiveDataObserve() {
    }

}