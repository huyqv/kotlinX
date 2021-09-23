package com.kotlin.app.ui.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kotlin.app.R
import com.sample.library.extension.hideKeyboard
import com.sample.library.util.Logger

abstract class BaseBottomDialog<T : ViewBinding> : BottomSheetDialogFragment(),
        FragmentView {

    protected val log: Logger by lazy { Logger(this::class) }

    protected val bind: T by viewBinding(inflating())

    abstract fun inflating(): (LayoutInflater) -> ViewBinding

    /**
     * [BottomSheetDialogFragment] implements
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = object : Dialog(requireActivity(), dialogStyle()) {
            override fun onBackPressed() {
                this@BaseBottomDialog.onBackPressed()
            }
        }
        dialog.setOnDismissListener {
            println("onDismiss")
        }
        return dialog
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = bind.root
        view.setOnTouchListener { _, _ -> true }
        configDialog()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log.d("onViewCreated")
        onViewCreated()
        onLiveDataObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        log.d("onDestroyView")
    }

    override fun onDismiss(dialog: DialogInterface) {
        hideKeyboard()
        launch(200) { super.onDismiss(dialog) }
    }

    /**
     * [FragmentView] implements
     */
    override val fragment: Fragment get() = this

    override val backPressedCallback: OnBackPressedCallback by lazy { getBackPressCallBack() }

    override fun onBackPressed() {
        backPressedCallback.remove()
        dismissAllowingStateLoss()
    }

    /**
     * [BaseBottomDialog] properties
     */
    protected open fun dialogStyle(): Int {
        return R.style.App_Dialog
    }

    private fun configDialog() {
        val bottomDialog = dialog as BottomSheetDialog
        val bottomSheet = bottomDialog.findViewById<View>(R.id.design_bottom_sheet)
        val coordinatorLayout = bottomSheet?.parent as? CoordinatorLayout ?: return
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.peekHeight = bottomSheet.height
        coordinatorLayout.parent.requestLayout()
    }

}


