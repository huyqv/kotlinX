package com.huy.kotlin.ui.member

import android.os.Bundle
import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.dialog.*
import com.huy.kotlin.base.view.BaseFragment
import com.huy.library.extension.showConfirmDialog
import com.huy.library.extension.showDialog
import com.huy.library.extension.showMessageDialog
import com.huy.library.extension.toast
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

    override val layoutResource: Int = R.layout.fragment_dialogs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addClickListener(circularProgress, appBarProgress, topProgress, bottomProgress,
                customConfirm, customMessage, message, confirm, allConfig)
    }

    override fun onViewClick(v: View?) {
        super.onViewClick(v)
        when (v) {
            circularProgress -> ProgressCircularDialog(requireActivity()).show()

            appBarProgress -> appBarView.progressVisible = true

            topProgress -> ProgressDialog(requireActivity()).show()

            bottomProgress -> ProgressBottomDialog(requireActivity()).show()

            message -> context?.showMessageDialog(null, "Message")

            customMessage -> MessageDialog(activity).run {
                title("This is title")
                message("Message goes here")
                drawable(R.drawable.ic_adb)
                show()
            }

            confirm -> context?.showConfirmDialog(null, "Message") {
                toast("Hello World !")
            }

            customConfirm -> ConfirmDialog(activity).run {
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