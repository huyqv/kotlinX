package com.kotlin.sample.ui.dialog

import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.kotlin.app.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/12
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ProgressBottomDialog(activity: FragmentActivity?) : BaseAlertDialog(activity) {

    /**
     * [BaseAlertDialog] implement
     */
    override fun layoutRes(): Int {
        return R.layout.progress_bottom
    }

    override fun theme(): Int {
        return R.style.App_Dialog
    }

    override fun View.onViewCreated() {
        val window = self?.window ?: return
        val wlp = window.attributes
        wlp.gravity = Gravity.BOTTOM
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = wlp
    }

}