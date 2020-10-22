package com.example.kotlin.base.arch

import android.os.Bundle
import com.example.kotlin.base.ext.viewModel
import com.example.kotlin.base.view.BaseActivity
import com.example.kotlin.base.vm.BaseViewModel
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