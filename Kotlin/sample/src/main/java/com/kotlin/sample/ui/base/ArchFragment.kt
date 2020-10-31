package com.kotlin.sample.ui.base

import android.os.Bundle
import android.view.View
import com.kotlin.app.base.ext.viewModel
import com.kotlin.app.base.view.BaseFragment
import com.kotlin.app.base.vm.BaseViewModel
import com.example.library.extension.networkLiveData
import kotlin.reflect.KClass

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/06/30
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class ArchFragment<VM : BaseViewModel> : BaseFragment() {

    protected val localVM: VM by lazy { viewModel(localViewModelClass()) }

    protected abstract fun localViewModelClass(): KClass<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localVM.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onLiveDataObserve()
        networkLiveData.observe {
            if (it) localVM.onNetworkAvailable()
        }
    }

}