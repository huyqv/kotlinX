package com.huy.kotlin.network.notification

import android.os.Bundle
import com.huy.kotlin.R
import com.huy.kotlin.base.view.BaseActivity
import com.huy.kotlin.extension.toast

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/19
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class NotificationActivity : BaseActivity() {

    override fun layoutResource() = R.layout.view_gone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toast("notify message")
    }
}