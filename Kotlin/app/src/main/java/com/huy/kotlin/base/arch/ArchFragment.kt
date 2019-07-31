package com.huy.kotlin.base.arch

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.huy.kotlin.base.view.BaseFragment

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/06/30
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class ArchFragment<VM : BaseViewModel> : BaseFragment() {

    protected lateinit var viewModel: VM

    protected abstract fun viewModelClass(): Class<VM>

    protected abstract fun onCreated(state: Bundle?)

    protected abstract fun onRegisterLiveData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModel(viewModelClass())

        viewModel.onStart()

        onCreated(savedInstanceState)

        onRegisterLiveData()

    }

    fun <T : ViewModel> viewModel(cls: Class<T>): T {
        return ViewModelProviders.of(this).get(cls)
    }

}