package com.huy.kotlin.base.arch

import android.os.Bundle
import com.huy.kotlin.base.view.BaseActivity
import com.huy.kotlin.data.observable.AlertLiveData
import com.huy.kotlin.data.observable.NetworkLiveData
import com.huy.kotlin.data.observable.ProgressLiveData
import com.huy.kotlin.extension.viewModel

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

        ProgressLiveData.instance.nonNull { if (it) showProgress() else hideProgress() }

        AlertLiveData.instance.nonNull { alert(it) }

        NetworkLiveData.instance.nonNull { if (it) viewModel.onNetworkAvailable() }

        viewModel.onStart()

        onCreated(savedInstanceState)

        onRegisterLiveData()

        viewModel.onNetworkAvailable()

    }

}