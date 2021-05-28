package com.kotlin.app.ui.base

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.library.extension.hideSystemUI

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2021/05/28
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseAlertDialog {

    companion object {
        val dialogList: MutableList<AlertDialog?> = mutableListOf()
    }

    protected var self: AlertDialog? = null

    protected lateinit var view: View

    val context: Context? get() = view.context

    val isShown: Boolean
        get() {
            self ?: return false
            return self!!.isShowing
        }

    constructor(activity: FragmentActivity?) {
        activity ?: return
        view = LayoutInflater.from(activity).inflate(layoutRes(), null)
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity, theme())
        builder.setView(view)
        self = builder.create()
        self?.hideSystemUI()
        if (activity is BaseActivity) dialogList.add(self)
        if (hasShadow()) setShadow()
        onDismiss {}
        onShow {}
        view.onViewCreated()
        view.isFocusable = false
        view.isFocusableInTouchMode = true
    }

    @StyleRes
    protected open fun theme(): Int = 0

    protected open fun hasShadow(): Boolean = false

    @LayoutRes
    protected abstract fun layoutRes(): Int

    protected abstract fun View.onViewCreated()

    fun setShadow() {
        val wlp = self?.window?.attributes ?: return
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        self?.window?.attributes = wlp
    }

    fun setGravityBottom() {
        val wlp = self?.window?.attributes ?: return
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        wlp.gravity = Gravity.BOTTOM
        self?.window?.attributes = wlp
    }

    fun onShow(block: () -> Unit) {
        self?.setOnShowListener {
            onShow()
            block.apply { block() }
        }
    }

    open fun onShow() {

    }

    fun onDismiss(block: () -> Unit) {
        self?.setOnDismissListener {
            onDismiss()
            block.apply { block() }
        }
    }

    open fun onDismiss() {

    }

    fun disableOnTouchOutside() {
        self?.setCanceledOnTouchOutside(false)
    }

    open fun show() {
        if (isShown) return
        try {
            self?.show()
        } catch (ignore: WindowManager.BadTokenException) {
        }
    }

    open fun dismiss() {
        if (isShown) self?.dismiss()
    }

}