package com.example.library.extension

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/10/18
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
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

fun <T> Flow<Data<T>>.each(vm: ViewModel, block: (Data<T>) -> Unit) {
    flowOn(Dispatchers.IO).onEach {
        block(it)
    }.launchIn(vm.viewModelScope)
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