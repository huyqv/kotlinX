package com.example.library.extension

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*

fun <T, R> Collection<T>.transform(block: (T) -> R?): List<R> {
    val list = mutableListOf<R>()
    for (item in this) {
        block(item)?.also {
            list.add(it)
        }
    }
    return list
}

/**
 * Typed T should be override method toString() : String
 */
fun <T> Collection<T>?.search(s: String?, searchProperty: (T) -> String?): Collection<T>? {
    return this?.filter { searchProperty(it).like(s) }
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

val uiHandler: Handler by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    Handler(Looper.getMainLooper())
}

fun post(block: () -> Unit) {
    uiHandler.post { block() }
}

fun post(delay: Long, block: () -> Unit) {
    uiHandler.postDelayed({ block() }, delay)
}

typealias Block<T> = ((T) -> Unit)?

fun <T> Block<T>.does(t: T) {
    this?.also { it(t) }
}