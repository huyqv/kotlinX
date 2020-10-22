package com.example.kotlin.base.arch

import android.os.Bundle
import android.view.View
import com.example.kotlin.base.ext.viewModel
import com.example.kotlin.base.view.BaseDialog
import com.example.kotlin.base.vm.BaseViewModel
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