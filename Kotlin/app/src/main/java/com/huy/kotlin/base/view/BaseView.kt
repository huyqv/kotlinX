package com.huy.kotlin.base.view

import androidx.fragment.app.Fragment
import com.huy.kotlin.base.dialog.ConfirmDialog
import com.huy.kotlin.base.dialog.MessageDialog

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface BaseView {

    val baseActivity: BaseActivity?

    fun alert(message: String?) {
        message ?: return
        MessageDialog(baseActivity).apply {
            message(message)
            show()
        }
    }

    fun alertConfirm(message: String?, block: () -> Unit) {
        message ?: return
        ConfirmDialog(baseActivity).run {
            message(message)
            onConfirm(block)
            show()
        }
    }

    fun showProgress()

    fun hideProgress()

    fun moveTaskToBack() {
        baseActivity?.moveTaskToBack(true)
    }

    fun <T : Fragment> popBackStack(cls: Class<T>) {
        baseActivity?.remove(cls)
    }

    fun popBackStack() {
        baseActivity?.supportFragmentManager?.popBackStack()
    }

}