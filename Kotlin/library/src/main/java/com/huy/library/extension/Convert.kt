package com.huy.library.extension

import java.text.ParseException
import java.util.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/2/24
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
const val BYTE = 1.toByte()

fun Boolean.toByte(): Byte {
    return if (this) 1 else 0
}

fun Byte.toBool(): Boolean {
    return this == BYTE
}

fun Long.parse(): Date? {

    return try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        calendar.time
    } catch (e: ParseException) {
        e.printStackTrace()
        null
    }
}

fun <T> nonNull(block: (T) -> Unit): (T?) -> Unit {
    return {
        if (it != null) block(it)
    }
}
