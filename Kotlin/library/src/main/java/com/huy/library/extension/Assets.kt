package com.huy.library.extension

import com.huy.library.Library
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/09/09
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun readBytes(fileName: String): ByteArray? {
    return try {
        val inputStream = Library.app.assets.open(fileName)
        val bytes = ByteArray(inputStream.available())
        inputStream.read(bytes)
        inputStream.close()
        return bytes
    } catch (e: FileNotFoundException) {
        null
    }

}

fun readString(filename: String): String? {
    return try {
        val sb = StringBuilder()
        BufferedReader(InputStreamReader(Library.app.assets.open(filename))).useLines { lines ->
            lines.forEach {
                sb.append(it)
            }
        }
        return sb.toString()
    } catch (e: FileNotFoundException) {
        null
    }

}

