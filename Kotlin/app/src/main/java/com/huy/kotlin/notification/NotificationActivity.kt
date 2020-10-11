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

    override fun layoutResource(): Int {
        return R.layout.view_gone
    }

    override fun onViewCreated() {
        toast("notify message")
    }
}