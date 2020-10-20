package com.example.library.extension

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/10/18
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun Collection<Any>?.isNullOrEmpty(): Boolean {
    return this == null || isEmpty()
}

fun Collection<Any>?.notNullOrEmpty(): Boolean {
    return this != null && isNotEmpty()
}

fun <T> Collection<T>?.isSingleElement(): Boolean {
    this ?: return false
    return this.size == 1
}

fun <T> join(vararg collections: Collection<T>?): List<T> {
    val list = mutableListOf<T>()
    collections.forEach { if (!it.isNullOrEmpty()) list.addAll(it) }
    return list
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

fun <T> Collection<T?>?.filters(block: (T) -> T?): List<T>? {
    this ?: return null
    val list = mutableListOf<T>()
    for (item in this) {
        item ?: continue
        val filterItem = block(item) ?: continue
        list.add(filterItem)
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