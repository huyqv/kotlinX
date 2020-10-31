package com.kotlin.sample.ui.base

import android.os.Bundle
import com.kotlin.app.base.ext.viewModel
import com.kotlin.app.base.view.BaseActivity
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
abstract class ArchActivity<VM : BaseViewModel> : BaseActivity() {

    protected val localVM: VM by lazy { viewModel(localViewModelClass()) }

    protected abstract fun localViewModelClass(): KClass<VM>

    protected abstract fun onCreated(state: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreated(savedInstanceState)
        localVM.onStart()
        networkLiveData.observe {
            if (it) localVM.onNetworkAvailable()
        }
    }

}