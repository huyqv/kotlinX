package com.sample.library.extension

import java.text.Normalizer
import java.util.regex.Pattern
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
                .toLowerCase()
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
