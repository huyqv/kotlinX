package com.huy.kotlin.base.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Handler
import android.os.SystemClock
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.huy.kotlin.app.App
import com.huy.kotlin.base.dialog.ConfirmDialog
import com.huy.kotlin.base.dialog.MessageDialog
import com.huy.kotlin.base.dialog.ProgressDialog
import com.huy.library.extension.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface BaseView : LifecycleOwner {

    fun fragmentActivity(): FragmentActivity?


    /**
     * [View.OnClickListener] implementation
     */
    var lastClickTime: Long

    var onViewClick: View.OnClickListener?

    fun onViewClick(view: View)

    fun addClickListener(vararg views: View) {

        if (views.isEmpty()) return

        if (onViewClick == null)
            onViewClick = View.OnClickListener { v ->

                v.preventClick()

                if (SystemClock.elapsedRealtime() - lastClickTime > 360) onViewClick(v)

                lastClickTime = SystemClock.elapsedRealtime()

                (v.context as? Activity)?.hideKeyboard()
            }

        for (v in views) v.setOnClickListener(onViewClick)
    }


    /**
     * [ProgressDialog] implementation
     */
    var progressDialog: ProgressDialog?

    fun getProgress(): ProgressDialog {
        if (null == progressDialog) {
            progressDialog = ProgressDialog(fragmentActivity())
        }
        return progressDialog!!
    }

    fun showProgress() {
        if (progressDialog == null)
            progressDialog = ProgressDialog(fragmentActivity())
        getProgress().show()
    }

    fun hideProgress() {
        Handler().postDelayed({
            getProgress().dismiss()
        }, 100)
    }


    /**
     * Fragment utilities
     */
    @IdRes
    fun fragmentContainer(): Int?

    fun add(fragment: Fragment, stack: Boolean = true) {
        fragmentContainer()?.also {
            fragmentActivity()?.addFragment(fragment, it, stack)
        }
    }

    fun replace(fragment: Fragment, stack: Boolean = true) {
        fragmentContainer()?.also {
            fragmentActivity()?.replaceFragment(fragment, it, stack)
        }
    }


    /**
     * Widget utilities
     */
    fun networkError() {

    }

    fun networkConnected(): Boolean {
        val cm = App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = cm.activeNetworkInfo
        val connected = activeNetworkInfo != null && activeNetworkInfo.isConnected
        if (!connected) networkError()
        return connected
    }

    fun networkDisconnected(): Boolean {
        return !networkConnected()
    }

    fun alert(@StringRes message: Int) {
        alert(string(message))
    }

    fun alert(message: String?) {
        message ?: return
        MessageDialog(fragmentActivity()).run {
            message(message)
            show()
        }
    }

    fun alertConfirm(@StringRes message: Int, block: () -> Unit) {
        alertConfirm(string(message), block)
    }

    fun alertConfirm(message: String?, block: () -> Unit) {
        message ?: return
        ConfirmDialog(fragmentActivity()).run {
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
        return Intent(fragmentActivity(), cls)
    }

    fun start(cls: Class<*>) {
        fragmentActivity()?.startActivity(Intent(fragmentActivity(), cls))
    }

    fun startFinish(cls: Class<*>) {
        fragmentActivity()?.run {
            startActivity(Intent(fragmentActivity(), cls))
            finish()
        }
    }

    fun startClear(cls: Class<*>) {
        fragmentActivity()?.run {
            val intent = Intent(this, cls)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            this.startActivity(intent)
            this.finish()
        }

    }

    fun moveTaskToBack() {
        fragmentActivity()?.moveTaskToBack(true)
    }

    fun popBackStack(cls: Class<*>) {
        fragmentActivity()?.remove(cls)
    }

    fun popBackStack()

    fun <T> LiveData<T?>.observe(block: (T?) -> Unit) {
        observe(this@BaseView, Observer { block(it) })
    }

    fun <T> LiveData<T?>.nonNull(block: (T) -> Unit) {
        observe(this@BaseView, Observer { if (null != it) block(it) })
    }

    fun <T> LiveData<T>.stop() {
        removeObservers(this@BaseView)
    }

}