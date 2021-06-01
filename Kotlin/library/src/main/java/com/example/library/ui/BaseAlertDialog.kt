package com.example.library.ui

import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import com.example.library.R
import com.example.library.extension.ViewClickListener
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
        val dialogList: MutableList<DialogInterface> = mutableListOf()
    }

    protected var dialog: AlertDialog? = null
        private set

    protected var view: View? = null
        private set

    val context: Context? get() = dialog?.context

    /**
     *
     */
    constructor(context: Context?) {
        context ?: return
        view = LayoutInflater.from(context).inflate(layoutResource(), null).also {
            dialog = AlertDialog.Builder(context, dialogTheme())
                    .setView(view)
                    .create()
            dialog?.hideSystemUI()

            onDismiss {}
            onShow {}
            it.isFocusable = false
            it.isFocusableInTouchMode = true
            it.onViewCreated()
        }
    }

    @LayoutRes
    protected abstract fun layoutResource(): Int

    protected abstract fun View.onViewCreated()


    /**
     *
     */
    val isShown: Boolean
        get() {
            dialog ?: return false
            return dialog!!.isShowing
        }

    @StyleRes
    protected open fun dialogTheme(): Int = R.style.App_Dialog

    protected open fun onViewClick(v: View?) = Unit

    protected open fun onWindowConfig(lp: WindowManager.LayoutParams) = Unit

    protected open fun onShow() = Unit

    protected open fun onDismiss() = Unit


    /**
     *
     */
    fun addClickListener(vararg views: View?) {
        val listener = object : ViewClickListener() {
            override fun onClicks(v: View?) {
                onViewClick(v)
            }
        }
        views.forEach { it?.setOnClickListener(listener) }
    }

    fun show() {
        if (isShown) return
        try {
            dialog?.show()
        } catch (ignore: WindowManager.BadTokenException) {
        }
    }

    fun onShow(block: () -> Unit) {
        dialog?.setOnShowListener {
            dialogList.add(it)
            onShow()
            block.apply { block() }
        }
    }

    fun dismiss() {
        if (isShown) dialog?.dismiss()
    }

    fun onDismiss(block: () -> Unit) {
        dialog?.setOnDismissListener {
            dialogList.remove(it)
            onDismiss()
            block.apply { block() }
        }
    }

    fun disableOnTouchOutside() {
        dialog?.setCanceledOnTouchOutside(false)
    }

    fun setShadow() {
        val wlp = dialog?.window?.attributes ?: return
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        dialog?.window?.attributes = wlp
    }

    fun setGravityBottom() {
        val wlp = dialog?.window?.attributes ?: return
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        wlp.gravity = Gravity.BOTTOM
        dialog?.window?.attributes = wlp
    }

}