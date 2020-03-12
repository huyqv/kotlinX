package com.huy.library.extension

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/10/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val handler = Handler()

fun post(block: () -> Unit) {
    handler.post { block() }
}

fun post(delay: Long, block: () -> Unit) {
    handler.postDelayed({ block() }, delay)
}


val ioExecutor: ExecutorService get() = Executors.newSingleThreadExecutor()

fun ioThread(block: () -> Unit) {
    ioExecutor.execute(block)
}


val uiHandler: Handler = Handler(Looper.getMainLooper())

val isOnUiThread: Boolean get() = Looper.myLooper() == Looper.getMainLooper()

fun uiThread(block: () -> Unit) {
    if (isOnUiThread) block()
    else uiHandler.post { block() }
}


