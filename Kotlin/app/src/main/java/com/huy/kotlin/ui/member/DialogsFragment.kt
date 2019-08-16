package com.huy.kotlin.ui.member

import android.os.Bundle
import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.dialog.*
import com.huy.kotlin.base.view.BaseFragment
import com.huy.library.extension.*
import kotlinx.android.synthetic.main.fragment_dialogs.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/30
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class DialogsFragment : BaseFragment() {

    override fun layoutResource() = R.layout.fragment_dialogs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addClickListener(circularProgress, appBarProgress, topProgress, bottomProgress,
                defaultProgress, horizontalProgressDialog, message, confirm, allConfig)
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view) {
            circularProgress -> ProgressCircularDialog(activity!!).show()

            appBarProgress -> appBarView.progressVisible = true

            topProgress -> ProgressDialog(activity!!).show()

            bottomProgress -> ProgressBottomDialog(activity!!).show()

            defaultProgress -> context?.getProgressDialog()?.show()

            horizontalProgressDialog -> LoadingDialog(activity!!).show()

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

    override fun popBackStack() {
        if (appBarView.progressVisible) {
            appBarView.progressVisible = false
            return
        }
        super.popBackStack()
    }

}