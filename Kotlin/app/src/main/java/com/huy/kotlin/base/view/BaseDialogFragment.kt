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
import com.huy.library.view.ViewClickListener
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
    final override val baseActivity: BaseActivity? get() = activity as? BaseActivity

    final override val progressDialog: ProgressDialog? get() = baseActivity?.progressDialog

    final override val fragmentContainerId: Int? get() = baseActivity?.fragmentContainerId

    final override val onViewClick: ViewClickListener by lazy {
        object : ViewClickListener() {
            override fun onViewClick(v: View?) {
                this@BaseDialogFragment.onViewClick(v)
            }
        }
    }

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

    open fun onBackPressed() {
        activity?.onBackPressed()
    }

    /**
     * Observer
     */
    fun <T> LiveData<T>.observe(block: (T) -> Unit) {
        observe(viewLifecycleOwner, Observer { block(it) })
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