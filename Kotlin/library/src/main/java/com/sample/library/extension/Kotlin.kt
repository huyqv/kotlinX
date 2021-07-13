package com.sample.library.extension

import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

typealias Void = () -> Unit

fun Void?.does() {
    this?.also { it() }
}

typealias Block<reified T> = T.() -> Unit

fun <T> Block<T>?.does(t: T) {
    this?.also { t.it() }
}

data class Data<T>(val data: T?, val e: Exception?)

fun <T> emit(block: suspend FlowCollector<Data<T>>.() -> T): Flow<Data<T>> {
    return flow {
        try {
            emit(Data(this.block(), null))
        } catch (e: Exception) {
            emit(Data<T>(null, e))
        }
    }
}

fun <T> Flow<Data<T>>.each(block: (Data<T>) -> Unit) {
    flowOn(Dispatchers.IO).onEach {
        block(it)
    }.launchIn(GlobalScope)
}

val isOnUiThread: Boolean get() = Looper.myLooper() == Looper.getMainLooper()

fun LifecycleOwner.launch(interval: Long = 0, block: Void) {
    lifecycleScope.launch {
        if (interval > 0) delay(interval)
        withContext(Dispatchers.Main) {
            block()
        }
    }
}

fun onUi(interval: Long = 0, block: Void) {
    GlobalScope.launch {
        if (interval > 0) delay(interval)
        withContext(Dispatchers.Main) {
            block()
        }
    }

}

fun onIo(interval: Long = 0, block: Void) {
    flow {
        if (interval > 0) delay(interval)
        emit(true)
    }.flowOn(Dispatchers.IO).onEach {
        block()
    }.launchIn(GlobalScope)
}