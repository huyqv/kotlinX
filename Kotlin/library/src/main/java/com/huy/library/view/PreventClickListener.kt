package com.huy.library.view

import android.app.Activity
import android.os.SystemClock
import android.view.View
import com.huy.library.extension.hideKeyboard

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2020/01/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class PreventClickListener : View.OnClickListener {

    private var lastClickTime: Long = 0

    final override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - lastClickTime > 600) {
            (v?.context as? Activity)?.hideKeyboard()
            onViewClick(v)
        }
        lastClickTime = SystemClock.elapsedRealtime()
    }

    abstract fun onViewClick(v: View?)

}