package com.huy.kotlin.base.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.huy.kotlin.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/17
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseDialog {

    protected var self: AlertDialog? = null

    protected var view: View? = null

    val context: Context? get() = view?.context

    constructor(activity: FragmentActivity?) {
        activity ?: return
        view = LayoutInflater.from(activity).inflate(layoutRes(), null)
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity, theme())
        builder.setView(view)
        self = builder.create()
        onDismiss {}
        onShow {}
        view!!.onViewCreated()

    }

    @StyleRes
    protected open fun theme() = R.style.Dialog_Custom

    @LayoutRes
    protected abstract fun layoutRes(): Int

    protected abstract fun View.onViewCreated()

    fun onShow(block: () -> Unit) {
        self?.setOnShowListener {
            onShow()
            block()
        }
    }

    protected open fun onShow() {
    }

    fun onDismiss(block: () -> Unit) {
        self?.setOnDismissListener {
            onDismiss()
            block()
        }
    }

    protected open fun onDismiss() {
    }

    fun dropListener() {
        self?.setOnShowListener(null)
        self?.setOnDismissListener(null)
    }

    fun disableOnTouchOutside() {
        self?.setCanceledOnTouchOutside(true)
    }

    fun show() {
        if (isShowing()) return
        self?.show()
    }

    fun dismiss() {
        if (isShowing()) self?.dismiss()
    }

    fun isShowing(): Boolean {
        self ?: return false
        return self!!.isShowing
    }

    fun isGone(): Boolean {
        self ?: return true
        return !self!!.isShowing
    }

}