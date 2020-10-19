package com.huy.kotlin.base.arch

import android.os.Bundle
import android.view.View
import com.huy.kotlin.base.view.BaseDialog
import com.huy.kotlin.util.viewModel
import com.huy.library.extension.networkLiveData

abstract class ArchDialog<VM : BaseViewModel> : BaseDialog() {

    protected val localVM: VM by lazy { viewModel(localViewModelClass()) }

    protected abstract fun localViewModelClass(): Class<VM>

    protected abstract fun onCreated(state: Bundle?)

    protected abstract fun onRegisterLiveData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreated(savedInstanceState)
        onRegisterLiveData()
        localVM.onStart()
        networkLiveData.observe {
            if (it) localVM.onNetworkAvailable()
        }
    }

}