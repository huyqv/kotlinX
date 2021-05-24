package com.kotlin.app.ui.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kotlin.app.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseBottomDialog : BottomSheetDialogFragment(), BaseView {

    /**
     * [BottomSheetDialogFragment] override
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, style())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
                this@BaseBottomDialog.onBackPressed()
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
        configDialog()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
        onLiveDataObserve()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        println("onDismiss")
    }

    /**
     * [BaseBottomDialog] Required implements
     */
    abstract fun layoutResource(): Int

    abstract fun onViewCreated()

    abstract fun onLiveDataObserve()

    /**
     * [BaseView] implement
     */
    override val baseActivity: BaseActivity? get() = activity as? BaseActivity

    override val lifecycleOwner: LifecycleOwner get() = viewLifecycleOwner

    /**
     * [BaseBottomDialog] properties
     */
    protected open fun style(): Int {
        return R.style.App_Dialog
    }

    protected open fun onBackPressed() {
        dismissAllowingStateLoss()
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


