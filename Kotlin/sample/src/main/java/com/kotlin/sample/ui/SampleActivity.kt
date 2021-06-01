package com.kotlin.sample.ui

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.library.extension.hideStatusBar
import com.example.library.ui.BaseActivity
import com.kotlin.sample.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/11/01
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SampleActivity  : BaseActivity() {

    override val navController: NavController? by lazy {
        findNavController(R.id.sampleFragment)
    }

    override fun layoutResource(): Int {
        return R.layout.sample
    }

    override fun onViewCreated() {
        hideStatusBar()
        addClickListener(

        )
    }

    override fun onLiveDataObserve() {
    }

    override fun onViewClick(v: View?) {
        when (v) {

        }
    }

}