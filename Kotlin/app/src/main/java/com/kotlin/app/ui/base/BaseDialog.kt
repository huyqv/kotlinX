package com.kotlin.app.ui.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.library.extension.hideKeyboard
import com.example.library.extension.post
import com.kotlin.app.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseDialog : DialogFragment(), BaseView {

    /**
     * [DialogFragment] override
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.App_DialogAnim
        setStyle(STYLE_NORMAL, dialogTheme())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = object : Dialog(requireActivity(), dialogTheme()) {
            override fun onBackPressed() {
                this@BaseDialog.onBackPressed()
            }
        }
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
        when (dialogTheme()) {
            R.style.App_Dialog_FullScreen,
            R.style.App_Dialog_FullScreen_Transparent,
            -> dialog?.window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        println("onDismiss")
    }

    override fun dismiss() {
        hideKeyboard()
        view.post(300) {
            super.dismiss()
        }
    }

    /**
     * [BaseDialog] Required implements
     */
    abstract fun layoutResource(): Int

    abstract fun onViewCreated()

    abstract fun onLiveDataObserve()

    /**
     * [BaseView] implement
     */
    override val baseActivity: BaseActivity? get() = activity as? BaseActivity

    override val lifecycleOwner: LifecycleOwner get() = viewLifecycleOwner

    override val navController: NavController? get() = findNavController()

    /**
     * [BaseDialog] properties
     */
    protected open fun dialogTheme(): Int {
        return R.style.App_Dialog
    }

    protected open fun onBackPressed() {
        dismissAllowingStateLoss()
    }

}