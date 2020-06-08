package com.huy.kotlin.base.view

import android.content.Intent
import android.content.res.Resources
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.huy.kotlin.base.dialog.ConfirmDialog
import com.huy.kotlin.base.dialog.MessageDialog
import com.huy.kotlin.base.dialog.ProgressDialog
import com.huy.library.extension.addFragment
import com.huy.library.extension.replaceFragment
import com.huy.library.extension.string
import com.huy.library.extension.toast
import com.huy.library.view.ViewClickListener

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface BaseView : LifecycleOwner {

    val baseActivity: BaseActivity?

    val layoutResource: Int

    /**
     * [View.OnClickListener] implementation
     */
    val onViewClick: ViewClickListener

    fun onViewClick(view: View?) {}

    fun addClickListener(vararg views: View?) {

        if (views.isEmpty()) return

        for (v in views) v?.setOnClickListener(onViewClick)
    }

    /**
     * [ProgressDialog] implementation
     */
    val progressDialog: ProgressDialog?

    fun showProgress() {
        baseActivity?.runOnUiThread {
            progressDialog?.show()
        }
    }

    fun hideProgress() {
        baseActivity?.runOnUiThread {
            progressDialog?.dismiss()
        }
    }

    /**
     * Fragment utilities
     */
    val fragmentContainerId: Int?
        get() {
            throw Resources.NotFoundException("BaseView.FragmentContainer() must be implement with resource id return value")
        }

    fun add(fragment: Fragment, stack: Boolean = true) {
        fragmentContainerId?.also {
            baseActivity?.addFragment(fragment, it, stack)
        }
    }

    fun replace(fragment: Fragment, stack: Boolean = true) {
        fragmentContainerId?.also {
            baseActivity?.replaceFragment(fragment, it, stack)
        }
    }

    fun remove(cls: Class<*>) {
        baseActivity?.remove(cls)
    }

    /**
     * Widget utilities
     */
    fun networkError() {

    }

    fun alert(@StringRes message: Int) {
        alert(string(message))
    }

    fun alert(message: String?) {
        message ?: return
        MessageDialog(baseActivity).run {
            message(message)
            show()
        }
    }

    fun alertConfirm(@StringRes message: Int, block: () -> Unit) {
        alertConfirm(string(message), block)
    }

    fun alertConfirm(message: String?, block: () -> Unit) {
        message ?: return
        ConfirmDialog(baseActivity).run {
            message(message)
            onConfirm(block)
            show()
        }
    }

    fun unknownError() {
        toast("Some thing went wrong :(")
    }

    /**
     * Navigation utilities
     */
    fun start(cls: Class<*>) {
        baseActivity?.startActivity(Intent(baseActivity, cls))
    }

    fun startFinish(cls: Class<*>) {
        baseActivity?.also {
            it.startActivity(Intent(it, cls))
            it.finish()
        }
    }

    fun startClear(cls: Class<*>) {
        baseActivity?.run {
            val intent = Intent(this, cls)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            this.startActivity(intent)
            this.finish()
        }

    }

    fun moveTaskToBack() {
        baseActivity?.moveTaskToBack(true)
    }

    fun popBackStack(cls: Class<*>) {
        baseActivity?.remove(cls)
    }

    fun popBackStack() {
        baseActivity?.supportFragmentManager?.popBackStack()
    }

}