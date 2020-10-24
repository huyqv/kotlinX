package com.example.kotlin.base.view

import android.view.View
import androidx.fragment.app.Fragment
import com.example.library.view.ViewClickListener

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

    fun showProgress() {
        baseActivity?.showProgress()
    }

    fun hideProgress() {
        baseActivity?.hideProgress()
    }

    fun alert(message: String?) {
        baseActivity?.alert(message)
    }

    fun alert(message: String?, block: () -> Unit) {
        baseActivity?.alert(message, block)
    }

    fun add(fragment: Fragment, stack: Boolean = true) {
        baseActivity?.add(fragment, stack)
    }

    fun replace(fragment: Fragment, stack: Boolean = true) {
        baseActivity?.replace(fragment, stack)
    }

    fun <T : Fragment> remove(cls: Class<T>) {
        baseActivity?.remove(cls)
    }

    fun addClickListener(vararg views: View?) {
        val listener = object : ViewClickListener() {
            override fun onClicks(v: View?) {
                onViewClick(v)
            }
        }
        views.forEach { it?.setOnClickListener(listener) }
    }

    fun onViewClick(v: View?) {}

}