package com.huy.kotlin.base.mvp

import com.huy.kotlin.base.view.BaseView
import java.lang.ref.WeakReference

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface BasePresenter<V : BaseView> {

    var viewRef: WeakReference<V>?

    val view: V?

    fun attach(view: V) {
        if (viewAttached()) return
        viewRef = WeakReference(view)
    }

    fun detach() {
        viewRef?.clear()
        viewRef = null
    }

    fun viewAttached(): Boolean {
        return null != view
    }

    fun viewDetached(): Boolean {
        return null == view
    }

    fun onStart()

    fun onStop()

}