package com.huy.library.extension

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/10/28
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val HANDLER = Handler()

val UI_HANDLER: Handler = Handler(Looper.getMainLooper())

val UI_EXECUTOR: Executor = Executor { command -> UI_HANDLER.post(command) }

val IO_HANDLER: ExecutorService get() = Executors.newSingleThreadExecutor()

val IO_EXECUTOR: Executor get() = Executor { command -> IO_HANDLER.execute(command) }

val isOnUiThread: Boolean get() = Looper.myLooper() == Looper.getMainLooper()

fun uiThread(block: () -> Unit) {
    if (isOnUiThread) block()
    else post { block() }
}

fun ioThread(block: () -> Unit) {
    IO_HANDLER.execute(block)
}

fun post(block: () -> Unit) {
    HANDLER.post { block() }
}

fun post(delay: Long, block: () -> Unit) {
    HANDLER.postDelayed({ block() }, delay)
}


