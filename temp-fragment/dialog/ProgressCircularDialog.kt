package com.kotlin.sample.ui.dialog

import android.view.Gravity
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.kotlin.sample.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/12
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ProgressCircularDialog(activity: FragmentActivity?) : BaseAlertDialog(activity) {

    /**
     * [BaseAlertDialog] implement
     */
    override fun layoutRes(): Int {
        return R.layout.progress_circular
    }

    override fun theme(): Int {
        return R.style.App_Dialog
    }

    override fun View.onViewCreated() {
        val window = self?.window ?: return
        val wlp = window.attributes
        wlp.gravity = Gravity.CENTER
        window.attributes = wlp
    }

}