package com.huy.kotlin.base.dialog

import android.view.Gravity
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.huy.kotlin.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/12
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ProgressCircularDialog(activity: FragmentActivity?) : BaseDialog(activity) {

    /**
     * [BaseDialog] implement
     */
    override fun layoutRes(): Int {
        return R.layout.dialog_progress_circular
    }

    override fun theme(): Int {
        return R.style.Dialog_Progress
    }

    override fun View.onViewCreated() {
        val window = self?.window ?: return
        val wlp = window.attributes
        wlp.gravity = Gravity.CENTER
        window.attributes = wlp
    }

}