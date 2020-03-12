package com.huy.kotlin.notification

import android.os.Bundle
import com.huy.kotlin.R
import com.huy.kotlin.base.view.BaseActivity
import com.huy.library.extension.toast

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/19
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class NotificationActivity : BaseActivity() {

    override val layoutResource = R.layout.view_gone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toast("notify message")
    }
}