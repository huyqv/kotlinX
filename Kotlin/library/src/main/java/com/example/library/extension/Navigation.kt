package com.example.library.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.library.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/22
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
const val DEFAULT_ARG_KEY: String = "default_arg_key"

fun Fragment.navigate(directions: NavDirections, block: (NavOptions.Builder.() -> Unit) = {}) {
    val option = NavOptions.Builder()
            .setDefaultAnim()
    option.block()
    findNavController().navigate(directions, option.build())
}

fun Fragment.navigateUp() {
    findNavController().navigateUp()
}

fun <T> Fragment.navResult(key: String = DEFAULT_ARG_KEY): T? {
    return findNavController().currentBackStackEntry?.savedStateHandle?.get<T>(key)
}

fun <T> Fragment.navResultLiveData(key: String = DEFAULT_ARG_KEY): MutableLiveData<T>? {
    return findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
}

fun <T> Fragment.setNavResult(key: String?, result: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key
            ?: DEFAULT_ARG_KEY, result)
}

fun <T> Fragment.setNavResult(result: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(DEFAULT_ARG_KEY, result)
}

fun NavOptions.Builder.setDefaultAnim(): NavOptions.Builder {
    setEnterAnim(R.anim.vertical_enter)
    setPopEnterAnim(R.anim.vertical_pop_enter)
    setExitAnim(R.anim.vertical_exit)
    setPopExitAnim(R.anim.vertical_pop_exit)
    return this
}