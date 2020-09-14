package com.huy.library.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.math.BigDecimal
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

fun ByteArray?.encodeToString(flag: Int = Base64.DEFAULT): String {
    return Base64.encodeToString(this, flag)
}

fun String.decodeToBytes(flag: Int = Base64.DEFAULT): ByteArray {
    return Base64.decode(this, flag)
}

fun String.decodeToBitmap(flag: Int = Base64.DEFAULT): Bitmap? {
    val bytes = decodeToBytes(flag)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun BigDecimal?.isNullOrZero(): Boolean {
    return this == null || this == BigDecimal.ZERO
}

