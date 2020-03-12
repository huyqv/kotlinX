package com.huy.kotlin.base.mvp

import android.os.Bundle
import com.huy.kotlin.base.view.BaseActivity
import com.huy.kotlin.base.view.BaseView

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class MvpActivity<V : BaseView, P : BasePresenter<V>> : BaseActivity() {

    protected var presenter: P? = null

    protected abstract fun presenter(): P?

    protected abstract fun onCreated(state: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = presenter()
        @Suppress("UNCHECKED_CAST")
        presenter?.attach(this as V)
        onCreated(savedInstanceState)
    }

    override fun onDestroy() {
        presenter?.detach()
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        presenter?.onStart()
    }

    override fun onStop() {
        presenter?.onStop()
        super.onStop()
    }

}

