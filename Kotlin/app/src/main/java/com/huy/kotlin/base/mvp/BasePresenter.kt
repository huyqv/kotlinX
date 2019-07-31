package com.huy.kotlin.base.mvp

import com.huy.kotlin.base.view.BaseView

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface BasePresenter<in V : BaseView> {

    fun attach(view: V)

    fun detach()

    fun viewAttached(): Boolean

    fun viewDetached(): Boolean

    fun onStart()

    fun onStop()

}