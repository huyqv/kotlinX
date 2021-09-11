package com.sample.library.extension

import java.text.Normalizer
import java.util.*
import java.util.regex.Pattern
import java.util.stream.Collectors

fun <T> List<T>.copyList(): MutableList<T> {
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

fun <T> List<T>?.join(collection: Collection<T>?): List<T>? {
    val list = mutableListOf<T>()
    if (!this.isNullOrEmpty()) {
        list.addAll(this)
    }
    if (!collection.isNullOrEmpty()) {
        list.addAll(collection)
    }
    return if (list.isEmpty()) return null else list
}

fun <T> Collection<T?>?.filters(block: (T) -> T?): MutableList<T>? {
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
fun <T> Collection<T>?.search(s: String?, searchProperty: (T) -> String?): Collection<T>? {
    return this?.filter { searchProperty(it).like(s) }
}

private fun String?.like(s: String?): Boolean {
    val left: String = this.normalizer() ?: return false
    val right: String = s.normalizer() ?: return false
    return left.contains(right) || right.contains(left)
}

private fun String?.normalizer(): String? {
    if (this.isNullOrEmpty()) return null
    return try {
        val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        pattern.matcher(temp)
                .replaceAll("")
                .lowercase(Locale.getDefault())
                .replace(" ", "-")
                .replace("đ", "d", true)
    } catch (e: IllegalStateException) {
        null
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun <T> List<T>.copy(): List<T> {
    return this.toList()
}
