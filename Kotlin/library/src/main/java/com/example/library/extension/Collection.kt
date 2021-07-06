package com.example.library.extension

import java.util.stream.Collectors

fun <T> java.util.List<T>.copyList(): MutableList<T> {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        this.stream().collect(Collectors.toList())
    } else {
        this.toMutableList()
    }

}

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

fun <T> List<T>.copy(): List<T> {
    return this.toList()
}
