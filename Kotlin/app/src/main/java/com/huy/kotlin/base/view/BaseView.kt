package com.huy.kotlin.base.view

import android.content.Intent
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.huy.kotlin.base.dialog.ConfirmDialog
import com.huy.kotlin.base.dialog.MessageDialog
import com.huy.kotlin.base.dialog.ProgressDialog
import com.huy.library.extension.*
import com.huy.library.view.PreventClickListener

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface BaseView : LifecycleOwner {

    val layoutResource: Int

    val baseActivity: BaseActivity?

    val fragmentActivity: FragmentActivity?

    /**
     * [View.OnClickListener] implementation
     */
    val onViewClick: PreventClickListener

    fun onViewClick(view: View?)

    fun addClickListener(vararg views: View?) {

        if (views.isEmpty()) return

        for (v in views) v?.setOnClickListener(onViewClick)
    }

    /**
     * [ProgressDialog] implementation
     */
    val progressDialog: ProgressDialog?

    fun showProgress() {
        fragmentActivity?.runOnUiThread {
            progressDialog?.show()
        }
    }

    fun hideProgress() {
        fragmentActivity?.runOnUiThread {
            progressDialog?.dismiss()
        }
    }

    /**
     * Fragment utilities
     */
    val fragmentContainer: Int? get() = null

    fun add(fragment: Fragment, stack: Boolean = true) {
        fragmentContainer?.also {
            fragmentActivity?.addFragment(fragment, it, stack)
        }
    }

    fun replace(fragment: Fragment, stack: Boolean = true) {
        fragmentContainer?.also {
            fragmentActivity?.replaceFragment(fragment, it, stack)
        }
    }

    fun remove(cls: Class<*>) {
        fragmentActivity?.remove(cls)
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
        MessageDialog(fragmentActivity).run {
            message(message)
            show()
        }
    }

    fun alertConfirm(@StringRes message: Int, block: () -> Unit) {
        alertConfirm(string(message), block)
    }

    fun alertConfirm(message: String?, block: () -> Unit) {
        message ?: return
        ConfirmDialog(fragmentActivity).run {
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
    fun getIntent(cls: Class<*>): Intent {
        return Intent(fragmentActivity, cls)
    }

    fun start(cls: Class<*>) {
        fragmentActivity?.startActivity(Intent(fragmentActivity, cls))
    }

    fun startFinish(cls: Class<*>) {
        fragmentActivity?.run {
            startActivity(Intent(fragmentActivity, cls))
            finish()
        }
    }

    fun startClear(cls: Class<*>) {
        fragmentActivity?.run {
            val intent = Intent(this, cls)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            this.startActivity(intent)
            this.finish()
        }

    }

    fun moveTaskToBack() {
        fragmentActivity?.moveTaskToBack(true)
    }

    fun popBackStack(cls: Class<*>) {
        fragmentActivity?.remove(cls)
    }

    fun popBackStack() {
        fragmentActivity?.supportFragmentManager?.popBackStack()
    }

}