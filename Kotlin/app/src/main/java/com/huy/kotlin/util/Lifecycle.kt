package com.huy.kotlin.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/09/29
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun LifecycleOwner.observe(block: (Lifecycle.Event) -> Unit) {
    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun onCreate() {
            block(Lifecycle.Event.ON_CREATE)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            block(Lifecycle.Event.ON_DESTROY)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() {
            block(Lifecycle.Event.ON_PAUSE)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            block(Lifecycle.Event.ON_RESUME)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onStart() {
            block(Lifecycle.Event.ON_START)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onStop() {
            block(Lifecycle.Event.ON_STOP)
        }
    })
}