package com.huy.kotlin.base.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.huy.kotlin.R
import com.huy.kotlin.base.dialog.ProgressDialog
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseDialogFragment : BottomSheetDialogFragment(), BaseView {


    override var lastClickTime: Long = 0

    override var onViewClick: View.OnClickListener? = null

    override var progressDialog: ProgressDialog? = null
        get() = activity()?.getProgress()

    @LayoutRes
    abstract fun layoutResource(): Int


    /**
     * [BaseView] implement
     */
    override fun fragmentActivity(): FragmentActivity? {
        return requireActivity()
    }

    override fun fragmentContainer(): Int? {
        return activity()?.fragmentContainer()
    }

    override fun onViewClick(view: View) {
    }

    override fun popBackStack() {
        activity()?.popBackStack()
    }


    /**
     * [BottomSheetDialogFragment] override
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_Fragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutResource(), container, false)
        view.setOnTouchListener { _, _ -> true }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onViewClick = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configDialog()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            onReceivedDataResult(requestCode, data)
        }
    }


    /**
     * [BottomSheetDialogFragment] utilities
     */
    open fun onReceivedDataResult(code: Int, intent: Intent) {
    }

    open fun onGrantedPermission(permission: String, block: () -> Unit) {
        RxPermissions(this)
                .request(permission)
                .subscribe { granted -> if (granted) block() }
    }

    open fun show(activity: FragmentActivity?) {
        activity ?: return
        super.show(activity.supportFragmentManager, this.javaClass.simpleName)
    }

    open fun activity(): BaseActivity? {
        //if (activity !is BaseActivity) throw ClassCastException("BaseFragment must be owned in BaseActivity")
        return activity as? BaseActivity
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