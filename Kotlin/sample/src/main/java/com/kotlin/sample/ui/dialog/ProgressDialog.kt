package com.kotlin.sample.ui.dialog

import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.kotlin.sample.R
import com.example.library.extension.color

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/12
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ProgressDialog(activity: FragmentActivity?) : BaseAlertDialog(activity) {

    /**
     * [BaseAlertDialog] implement
     */
    override fun layoutRes(): Int {
        return R.layout.progress_top
    }

    override fun theme(): Int {
        return R.style.App_Dialog
    }

    override fun View.onViewCreated() {
        val window = self?.window ?: return
        val wlp = window.attributes
        wlp.gravity = Gravity.TOP
        window.attributes = wlp
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color(R.color.colorPrimary)
        }
        window.attributes = wlp
    }

}