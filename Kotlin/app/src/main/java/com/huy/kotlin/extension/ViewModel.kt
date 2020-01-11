package com.huy.kotlin.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/12/23
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun <T : ViewModel> ViewModelStoreOwner.viewModel(cls: Class<T>): T = ViewModelProvider(this).get(cls)

fun <T : ViewModel> ViewModelStoreOwner.instanceViewModel(cls: Class<T>): T = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[cls]

