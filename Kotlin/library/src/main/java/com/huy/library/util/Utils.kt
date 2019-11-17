package com.huy.library.util

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/08/26
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object Utils {

    val currentMethod: String get() = Throwable().stackTrace[0].methodName
}