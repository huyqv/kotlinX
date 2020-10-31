package com.example.kotlin.ui.member

import android.view.View
import com.example.kotlin.R
import com.example.kotlin.base.dialog.ProgressBottomDialog
import com.example.kotlin.base.dialog.ProgressCircularDialog
import com.example.kotlin.base.dialog.ProgressDialog
import com.example.kotlin.base.view.BaseFragment
import com.example.library.extension.showConfirmDialog
import com.example.library.extension.showDialog
import com.example.library.extension.showMessageDialog
import com.example.library.extension.toast
import kotlinx.android.synthetic.main.fragment_dialogs.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/30
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class DialogsFragment : BaseFragment() {

    override fun layoutResource(): Int {
        return R.layout.sample_dialog
    }

    override fun onViewCreated() {
        addClickListener(circularProgress, appBarProgress, topProgress, bottomProgress,
                customConfirm, customMessage, message, confirm, allConfig)
    }

    override fun onLiveDataObserve() {
    }

    override fun onViewClick(v: View?) {
        super.onViewClick(v)
        when (v) {
            circularProgress -> ProgressCircularDialog(requireActivity()).show()

            appBarProgress -> appBarView.progressVisible = true

            topProgress -> ProgressDialog(requireActivity()).show()

            bottomProgress -> ProgressBottomDialog(requireActivity()).show()

            message -> context?.showMessageDialog(null, "Message")

            customMessage -> AlertDialog(activity).run {
                title("This is title")
                message("Message goes here")
                drawable(R.drawable.ic_adb)
                show()
            }

            confirm -> context?.showConfirmDialog(null, "Message") {
                toast("Hello World !")
            }

            customConfirm -> AlertDialog(activity).run {
                title("This is title")
                message("Message goes here")
                drawable(R.drawable.ic_adb)
                onConfirm {
                    toast("on confirm")
                }
                show()
            }

            allConfig -> context?.showDialog(getString(R.string.app_name),
                    "Hello, you can hide this message by just touch outside the dialog box.",
                    { toast("YES") },
                    { toast("NO") })
        }

    }

    override fun onBackPressed(): Boolean {
        if (appBarView.progressVisible) {
            appBarView.progressVisible = false
            return false
        }
        return super.onBackPressed()
    }

}