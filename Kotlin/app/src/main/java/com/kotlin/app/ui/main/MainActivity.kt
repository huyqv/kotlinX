package com.kotlin.app.ui.main

import com.example.library.extension.listenKeyboard
import com.kotlin.app.ui.base.BaseActivity
import com.kotlin.app.BuildConfig
import com.kotlin.app.R
import kotlinx.android.synthetic.main.main.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/29
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class MainActivity : BaseActivity() {

    override fun layoutResource(): Int {
        return R.layout.main
    }

    override fun navigationHostId(): Int {
        return R.id.mainFragment
    }

    override fun onViewCreated() {
        textViewVersion.text = "${BuildConfig.VERSION_NAME}-${BuildConfig.VERSION_CODE}"
        listenKeyboard()
    }

    override fun onLiveDataObserve() {
    }


}






