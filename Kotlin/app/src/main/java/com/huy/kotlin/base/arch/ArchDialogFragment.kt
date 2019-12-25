package com.huy.kotlin.base.arch

import android.os.Bundle
import android.view.View
import com.huy.kotlin.base.view.BaseDialogFragment
import com.huy.kotlin.data.observable.NetworkLiveData
import com.huy.kotlin.extension.viewModel

abstract class ArchDialogFragment<VM : BaseViewModel> : BaseDialogFragment() {

    protected val viewModel: VM by lazy { viewModel(viewModelClass) }

    protected abstract val viewModelClass: Class<VM>

    protected abstract fun onCreated(state: Bundle?)

    protected abstract fun onRegisterLiveData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onStart()

        onCreated(savedInstanceState)

        onRegisterLiveData()

        viewModel.onNetworkAvailable()

        NetworkLiveData.instance.nonNull { if (it) viewModel.onNetworkAvailable() }

    }

}