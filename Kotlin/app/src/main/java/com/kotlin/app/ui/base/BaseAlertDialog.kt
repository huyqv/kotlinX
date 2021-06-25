package com.kotlin.app.ui.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import com.example.library.extension.ViewClickListener
import com.kotlin.app.R

abstract class BaseAlertDialog {

    companion object {
        val dialogList: MutableList<DialogInterface> = mutableListOf()
    }

    protected var dialog: Dialog? = null

    lateinit var view: View
        private set

    @LayoutRes
    protected abstract fun layoutResource(): Int

    protected open fun onViewCreate() = Unit

    protected open fun onShow() = Unit

    protected open fun onDismiss() = Unit

    @StyleRes
    protected open fun dialogTheme(): Int = R.style.App_Dialog_FullScreen_Transparent

    protected open fun onViewClick(v: View?) = Unit

    protected open fun onWindowConfig(window: Window) = Unit

    open fun onBackPressed() {
        dialog?.dismiss()
    }

    constructor() {
        val activity = com.example.library.currentActivity ?: return
        view = LayoutInflater.from(activity).inflate(layoutResource(), null).also {
            it.isFocusable = false
            it.isFocusableInTouchMode = true
        }
        val sDialog: Dialog = object : Dialog(activity, dialogTheme()) {
            override fun onCreate(savedInstanceState: Bundle?) {
                onViewCreate()
            }

            override fun onBackPressed() {
                this@BaseAlertDialog.onBackPressed()
            }
        }
        dialog = sDialog.also {
            it.setCanceledOnTouchOutside(true)
            it.setContentView(view)
        }
        dialog?.window?.also {
            onWindowConfig(it)
        }
        onDismiss {}
        onShow {}

    }

    val isShowing: Boolean get() = dialog?.isShowing ?: false


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
        if (isShowing) return
        try {
            dialog?.show()
        } catch (ignore: WindowManager.BadTokenException) {
        }
    }

    fun onShow(block: () -> Unit) {
        dialog?.setOnShowListener {
            dialogList.add(it)
            block.apply { block() }
            onShow()
        }
    }

    fun dismiss() {
        if (isShowing) {
            dialog?.dismiss()
        }
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

    fun Window.setGravityBottom() {
        when {
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R -> {
                this.decorView.windowInsetsController?.hide(
                        WindowInsets.Type.statusBars()
                                or WindowInsets.Type.navigationBars()
                )
            }
            else -> {
                val wlp = this.attributes ?: return
                @Suppress("DEPRECATION")
                wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
                wlp.gravity = Gravity.BOTTOM
                dialog?.window?.attributes = wlp
            }
        }
    }

    fun Window.setFullScreen() {
        setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }

}