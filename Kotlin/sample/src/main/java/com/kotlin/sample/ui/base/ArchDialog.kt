package com.kotlin.sample.ui.base

import android.os.Bundle
import android.view.View
import com.kotlin.app.base.ext.viewModel
import com.kotlin.app.base.view.BaseDialog
import com.kotlin.app.base.vm.BaseViewModel
import com.example.library.extension.networkLiveData
import kotlin.reflect.KClass

abstract class ArchDialog<VM : BaseViewModel> : BaseDialog() {

    protected val localVM: VM by lazy { viewModel(localViewModelClass()) }

    protected abstract fun localViewModelClass(): KClass<VM>

    protected abstract fun onCreated(state: Bundle?)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreated(savedInstanceState)
        onLiveDataObserve()
        localVM.onStart()
        networkLiveData.observe {
            if (it) localVM.onNetworkAvailable()
        }
    }

}