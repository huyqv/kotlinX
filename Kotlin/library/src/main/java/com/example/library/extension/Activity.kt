package com.example.library.extension

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleObserver

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2021/04/16
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun FragmentActivity?.addObserver(observer: LifecycleObserver) {
    this?.lifecycle?.addObserver(observer)
}