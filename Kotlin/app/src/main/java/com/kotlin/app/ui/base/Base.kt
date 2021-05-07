package com.kotlin.app.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlin.reflect.KClass

fun <T : ViewModel> ViewModelStoreOwner.viewModel(cls: KClass<T>): T =
        ViewModelProvider(this).get(cls.java)

fun <T : ViewModel> ViewModelStoreOwner.newVM(cls: KClass<T>): T =
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[cls.java]

fun <T : ViewModel> Fragment.activityVM(cls: KClass<T>): T =
        ViewModelProvider(requireActivity()).get(cls.java)

fun <T : ViewModel> AppCompatActivity.activityVM(cls: KClass<T>): T =
        ViewModelProvider(this).get(cls.java)

inline fun <T> LiveData<T?>.observe(owner: LifecycleOwner, crossinline block: (t: T?) -> Unit) {
    this.observe(owner, Observer {
        block(it)
    })
}