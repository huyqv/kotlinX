package com.example.kotlin.base.dialog

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.kotlin.R
import kotlinx.android.synthetic.main.dialog_network.view.*
import kotlinx.android.synthetic.main.dialog_title.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/07/25
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class NetworkDialog(activity: FragmentActivity?) : BaseAlertDialog(activity) {

    /**
     * [BaseAlertDialog] implement
     */
    override fun layoutRes(): Int {
        return R.layout.dialog_network
    }

    override fun View.onViewCreated() {
        dialogTextViewTitle.setText(R.string.network_error)
        disableOnTouchOutside()
        viewClose.setOnClickListener {
            dismiss()
        }
    }

}