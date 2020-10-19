package com.huy.kotlin.base.mvp

import com.huy.kotlin.base.view.BaseView

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface BasePresenter<V : BaseView> {

    val view: V?

    val viewAttached: Boolean get() = null != view

    val viewDetached: Boolean get() = null == view

    fun attach(view: V)

    fun detach()

    fun onStart() {}

    fun onStop() {}

}