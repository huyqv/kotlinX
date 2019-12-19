package com.huy.kotlin.base.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
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
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseDialogFragment : BottomSheetDialogFragment(), BaseView {


    /**
     * [BaseView] implement
     */
    override val baseActivity: BaseActivity? get() = activity as? BaseActivity

    override val fragmentActivity: FragmentActivity? get() = requireActivity()

    override var onViewClick: View.OnClickListener? = null

    override var progressDialog: ProgressDialog? = null
        get() = baseActivity?.getProgress()


    /**
     * [BottomSheetDialogFragment] override
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutResource, container, false)
        view.setOnTouchListener { _, _ -> true }
        configDialog()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onViewClick = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            onReceivedDataResult(requestCode, data)
        }
    }

    override fun onViewClick(view: View) {
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

    open fun onBackPressed() {
        activity?.onBackPressed()
    }


    /**
     * Observer
     */
    fun <T> LiveData<T?>.observe(block: (T?) -> Unit) {
        observe(viewLifecycleOwner, Observer { block(it) })
    }

    fun <T> LiveData<T?>.nonNull(block: (T) -> Unit) {
        observe(viewLifecycleOwner, Observer { if (null != it) block(it) })
    }

    open fun show(activity: FragmentActivity?) {
        activity ?: return
        super.show(activity.supportFragmentManager, this.javaClass.simpleName)
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