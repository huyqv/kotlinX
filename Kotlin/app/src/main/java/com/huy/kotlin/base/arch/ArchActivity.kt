package com.huy.kotlin.base.arch

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
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

    protected lateinit var viewModel: VM

    protected abstract fun viewModelClass(): Class<VM>

    protected abstract fun onCreated(state: Bundle?)

    protected abstract fun onRegisterLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModel(viewModelClass())

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

    override fun getLifecycle(): Lifecycle {
        return super.getLifecycle()
    }

    fun <T : ViewModel> viewModel(cls: Class<T>): T {
        return ViewModelProviders.of(this).get(cls)
    }

}