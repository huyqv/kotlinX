package com.huy.kotlin.base.dialog

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.huy.kotlin.R
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
class NetworkDialog(activity: FragmentActivity?) : BaseDialog(activity) {

    /**
     * [BaseDialog] implement
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