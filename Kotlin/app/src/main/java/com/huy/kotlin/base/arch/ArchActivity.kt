package com.huy.kotlin.base.arch

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.huy.kotlin.base.view.BaseActivity
import com.huy.kotlin.data.observable.AlertLiveData
import com.huy.kotlin.data.observable.ProgressLiveData
import com.huy.kotlin.data.observable.ToastLiveData
import com.huy.library.extension.toast

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/06/30
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class ArchActivity<VM : BaseViewModel> : BaseActivity() {

    protected val viewModel: VM by lazy { viewModel(viewModelClass) }

    protected abstract val viewModelClass: Class<VM>

    protected abstract fun onCreated(state: Bundle?)

    protected abstract fun onRegisterLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onStart()

        ProgressLiveData.instance.nonNull { if (it) showProgress() else hideProgress() }

        AlertLiveData.instance.nonNull { alert(it) }

        ToastLiveData.instance.nonNull { toast(it) }

        onCreated(savedInstanceState)

        onRegisterLiveData()

    }

    override fun onDestroy() {
        super.onDestroy()

        ProgressLiveData.instance.removeObservers(this)

        AlertLiveData.instance.removeObservers(this)

        ToastLiveData.instance.removeObservers(this)
    }

    fun <T : ViewModel> viewModel(cls: Class<T>): T {
        return ViewModelProvider(this).get(cls)
    }

    fun <T : ViewModel> instanceViewModel(cls: Class<T>): T {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[cls]
    }


}