package com.huy.kotlin.base.arch

import android.os.Bundle
import android.view.View
import com.huy.kotlin.base.view.BaseFragment
import com.huy.kotlin.data.observable.NetworkLiveData
import com.huy.kotlin.util.viewModel

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/06/30
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class ArchFragment<VM : BaseViewModel> : BaseFragment() {

    protected val viewModel: VM by lazy { viewModel(viewModelClass) }

    protected abstract val viewModelClass: Class<VM>

    protected abstract fun onCreated(state: Bundle?)

    protected abstract fun onRegisterLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onCreated(savedInstanceState)

        onRegisterLiveData()

        viewModel.onNetworkAvailable()

        NetworkLiveData.instance.observe { if (it) viewModel.onNetworkAvailable() }

    }

}