package com.huy.kotlin.base.arch

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.huy.kotlin.base.view.BaseFragment


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onStart()

        onCreated(savedInstanceState)

        onRegisterLiveData()

    }

    fun <T : ViewModel> viewModel(cls: Class<T>): T {
        return ViewModelProvider(this).get(cls)
    }

    fun <T : ViewModel> instanceViewModel(cls: Class<T>): T {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[cls]
    }


}