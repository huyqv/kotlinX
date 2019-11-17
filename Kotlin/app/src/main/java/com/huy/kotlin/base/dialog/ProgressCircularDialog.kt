package com.huy.kotlin.base.dialog

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

    override fun layoutRes() = R.layout.dialog_progress_circular

    override fun theme() = R.style.Dialog_FullScreen

    override fun View.onViewCreated() {
        disableOnTouchOutside()
    }

}