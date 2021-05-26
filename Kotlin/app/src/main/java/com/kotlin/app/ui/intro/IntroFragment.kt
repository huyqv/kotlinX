package com.kotlin.app.ui.intro

import com.example.library.extension.post
import com.kotlin.app.MainDirections
import com.kotlin.app.R
import com.kotlin.app.ui.main.MainFragment
import kotlinx.android.synthetic.main.intro.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/11/01
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class IntroFragment : MainFragment() {

    override fun layoutResource(): Int {
        return R.layout.intro
    }

    override fun onViewCreated() {
        textView.setOnClickListener {
            post(1000) {
                navigate(MainDirections.actionGlobalHomeFragment())
            }
        }
    }

    override fun onLiveDataObserve() {
    }

}