package com.example.library.extension

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

val uiHandler: Handler by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    Handler(Looper.getMainLooper())
}


fun post(block: () -> Unit) {
    uiHandler.post { block() }
}

fun post(delay: Long, block: () -> Unit) {
    uiHandler.postDelayed({ block() }, delay)
}


val ioExecutor: ExecutorService get() = Executors.newSingleThreadExecutor()

fun ioThread(block: () -> Unit) {
    ioExecutor.execute(block)
}


val isOnUiThread: Boolean get() = Looper.myLooper() == Looper.getMainLooper()

fun uiThread(block: () -> Unit) {
    if (isOnUiThread) block()
    else uiHandler.post { block() }
}


