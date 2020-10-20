package com.example.kotlin.util

import androidx.fragment.app.Fragment
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
fun <T : ViewModel> ViewModelStoreOwner.viewModel(cls: Class<T>): T =
        ViewModelProvider(this).get(cls)

fun <T : ViewModel> ViewModelStoreOwner.newViewModel(cls: Class<T>): T =
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[cls]

fun <T : ViewModel> Fragment.activityViewModel(cls: Class<T>): T =
        ViewModelProvider(requireActivity()).get(cls)
