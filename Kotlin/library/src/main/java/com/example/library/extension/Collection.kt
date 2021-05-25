package com.example.library.extension

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
fun <T> Collection<T>?.search(s: String?): Collection<T>? {
    if (s.isNullOrEmpty() || this.isNullOrEmpty()) return this
    val list = mutableListOf<T>()
    for (model in this) {
        if (s.like(model?.toString())) list.add(model)
    }
    return list
}
