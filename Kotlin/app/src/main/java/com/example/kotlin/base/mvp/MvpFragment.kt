package com.example.kotlin.base.mvp

import android.os.Bundle
import android.view.View
import com.example.kotlin.base.view.BaseFragment
import com.example.kotlin.base.view.BaseView

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class MvpFragment<V : BaseView, P : BasePresenter<V>> : BaseFragment(), BaseView {

    protected abstract val presenter: P

    protected abstract fun onCreated(state: Bundle?)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        @Suppress("UNCHECKED_CAST")
        presenter.attach(this as V)
        onCreated(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

}