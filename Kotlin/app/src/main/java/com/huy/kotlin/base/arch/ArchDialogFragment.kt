package com.huy.kotlin.base.arch

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.huy.kotlin.base.view.BaseDialogFragment

abstract class ArchDialogFragment<VM : BaseViewModel> : BaseDialogFragment() {

    protected lateinit var viewModel: VM

    protected abstract fun viewModelClass(): Class<VM>

    protected abstract fun onCreated(state: Bundle?)

    protected abstract fun onRegisterLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModel(viewModelClass())

        viewModel.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onCreated(savedInstanceState)

        onRegisterLiveData()
    }

    fun <T : ViewModel> viewModel(cls: Class<T>): T {
        return ViewModelProviders.of(this).get(cls)
    }

}