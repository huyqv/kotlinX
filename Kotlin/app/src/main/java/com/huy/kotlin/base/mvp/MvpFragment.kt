package com.huy.kotlin.base.mvp

import android.os.Bundle
import android.view.View
import com.huy.kotlin.base.view.BaseFragment
import com.huy.kotlin.base.view.BaseView

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class MvpFragment<in V : BaseView, P : BasePresenter<V>> : BaseFragment() {

    protected var presenter: P? = null

    protected abstract fun presenter(): P?

    protected abstract fun onCreated(state: Bundle?)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = presenter()
        @Suppress("UNCHECKED_CAST")
        presenter?.attach(this as V)
        onCreated(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }

    override fun onStart() {
        super.onStart()
        presenter?.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

}