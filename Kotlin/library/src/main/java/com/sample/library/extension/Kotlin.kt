package com.sample.library.extension

import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

val isOnUiThread: Boolean get() = Looper.myLooper() == Looper.getMainLooper()

fun LifecycleOwner.launch(interval: Long = 0, block: () -> Unit) {
    lifecycleScope.launch {
        if (interval > 0) delay(interval)
        withContext(Dispatchers.Main) {
            block()
        }
    }
}

fun onUi(interval: Long = 0, block: () -> Unit) {
    GlobalScope.launch {
        if (interval > 0) delay(interval)
        withContext(Dispatchers.Main) {
            block()
        }
    }
}

fun onIo(interval: Long = 0, block: () -> Unit) {
    flow {
        if (interval > 0) delay(interval)
        emit(true)
    }.flowOn(Dispatchers.IO).onEach {
        block()
    }.launchIn(GlobalScope)
}

typealias ResultLiveData<T> = SingleLiveData<Result<T>>

typealias ResultFlow<T> = Flow<Result<T>>

fun <T : Any> tryFlow(block: suspend FlowCollector<Result<T>>.() -> T): Flow<Result<T>> {
    return flow {
        try {
            emit(Result.success(this.block()))
        } catch (e: Throwable) {
            emit(Result.failure<T>(e))
        }
    }.flowOn(Dispatchers.IO)
}

fun <T> Flow<T>.globalLaunch(block: suspend (T) -> Unit) {
    onEach(block).launchIn(GlobalScope)
}

fun sampleTryFlow() {
    tryFlow<String> {
        "Hello World"
    }.globalLaunch {
        toast(it.getOrNull())
    }
}
