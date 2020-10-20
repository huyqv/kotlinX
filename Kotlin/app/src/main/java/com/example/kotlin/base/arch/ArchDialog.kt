package com.example.kotlin.base.arch

import android.os.Bundle
import android.view.View
import com.example.kotlin.base.view.BaseDialog
import com.example.kotlin.util.viewModel
import com.example.library.extension.networkLiveData

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