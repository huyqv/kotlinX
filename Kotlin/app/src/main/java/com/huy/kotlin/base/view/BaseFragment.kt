package com.huy.kotlin.base.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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
abstract class BaseFragment : Fragment(), BaseView {

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
        return activity
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
     * [Fragment] override
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutResource(), container, false)
        view.setOnTouchListener { _, _ -> true }
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


    /**
     * [BaseFragment] utilities
     */
    open fun onReceivedDataResult(code: Int, intent: Intent) {
    }

    open fun onGrantedPermission(permission: String, block: () -> Unit) {
        RxPermissions(this)
                .request(permission)
                .subscribe { granted -> if (granted) block() }
    }

    open fun activity(): BaseActivity? {
        return activity as? BaseActivity
    }

}