package com.huy.kotlin.base.dialog

import android.app.Activity

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/10/01
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
@Suppress("DEPRECATION")
class LoadingDialog : android.app.ProgressDialog {

    constructor(activity: Activity) : super(activity) {
        isIndeterminate = false
        setProgressStyle(android.app.ProgressDialog.STYLE_HORIZONTAL)
        setCancelable(true)
        max = 100
    }

    fun update(value: Int) {
        progress = value
        if (progress >= max) {
            dismiss()
        }
    }


}