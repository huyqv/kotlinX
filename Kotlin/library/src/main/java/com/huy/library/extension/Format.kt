package com.huy.library.extension

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/08
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
private val DECIMAL_FMT = DecimalFormat("####,###,###")

private val NUMBER_FMT = NumberFormat.getNumberInstance(Locale.GERMANY)

fun String?.decimalFormat(): String? {
    this ?: return null
    return DECIMAL_FMT.format(this)
}

fun Long?.decimalFormat(): String? {
    this ?: return null
    return DECIMAL_FMT.format(this)
}

fun String?.numberFormat(): String? {
    this ?: return null
    return NUMBER_FMT.format(this)
}

fun Long?.numberFormat(): String? {
    this ?: return null
    return NUMBER_FMT.format(this)
}