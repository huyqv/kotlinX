package com.example.library.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.library.R
import com.example.library.extension.hideKeyboard
import com.example.library.extension.post

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseDialog : DialogFragment(),
        FragmentView {

    /**
     * [DialogFragment] implements
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = object : Dialog(requireActivity(), dialogStyle()) {
            override fun onBackPressed() {
                this@BaseDialog.onBackPressed()
            }
        }
        dialog.window?.attributes?.windowAnimations = R.style.App_DialogAnim
        dialog.setOnDismissListener {
            println("onDismiss")
        }
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutResource(), container, false)
        view.setOnTouchListener { _, _ -> true }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
        onLiveDataObserve()
    }

    override fun onStart() {
        super.onStart()
        when (dialogStyle()) {
            R.style.App_Dialog_FullScreen,
            R.style.App_Dialog_FullScreen_Transparent,
            -> dialog?.window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        hideKeyboard()
        view.post(300) {
            super.onDismiss(dialog)
        }
    }

    /**
     * [FragmentView] implements
     */
    override val fragment: Fragment get() = this

    /**
     * [BaseDialog] properties
     */
    protected open fun dialogStyle(): Int {
        return R.style.App_Dialog
    }

    protected open fun onBackPressed() {
        dismissAllowingStateLoss()
    }

}