package com.huy.kotlin.base.dialog

import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
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
class ProgressDialog(activity: FragmentActivity?) : BaseDialog(activity) {

    override fun layoutRes() = R.layout.dialog_progress_top

    override fun theme() = R.style.Dialog_FullScreen

    override fun View.onViewCreated() {

        val window = self?.window ?: return
        val layoutParams: WindowManager.LayoutParams = window.attributes ?: return

        layoutParams.gravity = Gravity.TOP
        layoutParams.flags = layoutParams.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(window.context, R.color.colorPrimary)
        }
        window.attributes = layoutParams
        disableOnTouchOutside()
    }

}