package com.example.kotlin.base.mvp

import android.os.Bundle
import com.example.kotlin.base.view.BaseActivity
import com.example.kotlin.base.view.BaseView

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class MvpActivity<V : BaseView, P : BasePresenter<V>> : BaseActivity(), BaseView {

    protected abstract val presenter: P

    protected abstract fun onCreated(state: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNCHECKED_CAST")
        presenter.attach(this as V)
        presenter.onStart()
        onCreated(savedInstanceState)
    }

    override fun onDestroy() {
        presenter.onStop()
        presenter.detach()
        super.onDestroy()
    }

}

