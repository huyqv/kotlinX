package com.huy.kotlin.base.dialog

import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.huy.kotlin.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/12
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ProgressBottomDialog(activity: FragmentActivity?) : BaseDialog(activity) {

    override fun layoutRes() = R.layout.dialog_progress_bottom

    override fun theme() = R.style.Dialog_FullScreen

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