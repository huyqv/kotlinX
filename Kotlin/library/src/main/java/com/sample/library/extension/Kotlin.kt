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

fun launch(interval: Long = 0, block: () -> Unit) {
    GlobalScope.launch {
        if (interval > 0) withContext(Dispatchers.IO) {
            delay(100)
        }
        withContext(Dispatchers.Main) {
            block()
        }
    }
}

typealias ResultLiveData<T> = SingleLiveData<Result<T>>

typealias ResultFlow<T> = Flow<Result<T>>

fun <T : Any> flowResult(block: suspend FlowCollector<Result<T>>.() -> T): Flow<Result<T>> {
    return flow {
        emit(runCatching { block() })
    }.flowOn(Dispatchers.IO)
}

fun <T> ioFlow(block: () -> T?): Flow<T?> {
    return flow {
        try {
            emit(withContext(Dispatchers.IO) { block() })
        } catch (e: Exception) {
            emit(null)
        }
    }.flowOn(Dispatchers.Main)
}

fun <T> Flow<T>.launch(block: (T) -> Unit) {
    onEach {
        block(it)
    }.launchIn(GlobalScope)
}

fun sampleTryFlow() {
    flowResult<String> {
        "Hello World"
    }.launch {
        toast(it.getOrNull())
    }
}
