package com.example.kotlin.notification

import com.example.kotlin.R
import com.example.kotlin.base.view.BaseActivity
import com.example.library.extension.toast

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/19
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class NotificationActivity : BaseActivity() {

    override fun layoutResource(): Int {
        return R.layout.view_gone
    }

    override fun onViewCreated() {
        toast("notify message")
    }
}