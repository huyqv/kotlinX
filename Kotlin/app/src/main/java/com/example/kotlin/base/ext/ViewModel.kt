package com.example.kotlin.base.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlin.reflect.KClass

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/12/23
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun <T : ViewModel> ViewModelStoreOwner.viewModel(cls: KClass<T>): T {
    return ViewModelProvider(this).get(cls.java)
}

fun <T : ViewModel> ViewModelStoreOwner.newViewModel(cls: KClass<T>): T {
    return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[cls.java]
}

fun <T : ViewModel> Fragment.activityViewModel(cls: KClass<T>): T {
    return ViewModelProvider(requireActivity()).get(cls.java)
}

fun <T : ViewModel> AppCompatActivity.activityViewModel(cls: KClass<T>): T {
    return ViewModelProvider(this).get(cls.java)
}


