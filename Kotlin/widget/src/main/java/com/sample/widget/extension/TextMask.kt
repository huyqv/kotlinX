package com.sample.widget.extension

import android.os.Build
import android.text.Html
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * 123456789012345 -> 1234 4567 8901 2345
 */
fun String?.toCreditNum(): String? {
    return if (this.isNullOrEmpty()) null else replace("\\d{4}".toRegex(), "$0 ")
}

/**
 * 123456789012345 -> •••• •••• •••• 2345
 */
fun String?.toHiddenCreditNum(): String {
    if (this == null || length < 17) return "•••• •••• •••• ••••"
    return "•••• •••• •••• ${substring(lastIndex - 4, lastIndex)}"
}

private val decimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat

fun Long?.moneyFormat(currency: String? = "VND"): String? {
    return this?.toString()?.moneyFormat(currency)
}

fun String?.moneyFormat(currency: String? = "VND"): String {
    this ?: return ""
    return try {
        if (currency != null && currency != "VND") {

            if (last().toString() == ".") return this

            val lgt = length
            if (lgt > 1 && substring(lgt - 2, lgt) == ".0") return this
            if (lgt > 2 && substring(lgt - 3, lgt) == ".00") return this

            val docId = indexOf(".")
            if (docId != -1 && substring(docId, length).length > 3) return substring(0, docId + 3)
        }
        var originalString = when (currency) {
            null, "VND" -> this.replace(".", "")
            else -> this
        }
        if (originalString.contains(",")) {
            originalString = originalString.replace(",".toRegex(), "")
        }
        when (currency) {
            null, "VND" -> {
                val value = originalString.toLong()
                decimalFormat.applyPattern("#,###,###,###")
                decimalFormat.format(value)
            }
            else -> {
                val value = originalString.toDouble()
                decimalFormat.applyPattern("#,###,###,###.##")
                decimalFormat.format(value)
            }
        }
    } catch (nfe: Exception) {
        ""
    }
}

fun String?.unHyper(): String? {
    this ?: return null
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> Html.fromHtml(this, 1).toString()
        else -> @Suppress("DEPRECATION")
        Html.fromHtml(this).toString()
    }
}

fun Long.cashToText(): String {

    val number = this.toString()

    var text = ""
    var startIndex = number.length - 3
    var endIndex = number.length
    var unit = " đồng"

    while (startIndex >= -2) {

        val sCash = number.substring(if (startIndex > -1) startIndex else 0, endIndex)

        text = " ${cashText(sCash)}$unit$text"
        startIndex -= 3
        endIndex -= 3
        unit = when (unit) {
            " nghìn" -> " triệu"
            " triệu" -> " tỷ"
            " đồng" -> " nghìn"
            else -> " nghìn"
        }
    }

    text = text.replace("  ", " ")
        .trim()
        .replace("tỷ triệu nghìn đồng", "tỷ đồng")
        .replace("triệu nghìn đồng", "triệu đồng")

    return text.substring(0, 1).uppercase(Locale.getDefault()) + text.substring(1, text.length)
}

/**
 * us -> 🇺🇸
 */
fun String?.flagIcon(): String {
    this ?: return ""
    if (length != 2) return ""
    val s = uppercase(Locale.getDefault())
    val char1st = Character.codePointAt(s, 0) - 0x41 + 0x1F1E6
    val char2st = Character.codePointAt(s, 1) - 0x41 + 0x1F1E6
    return String(Character.toChars(char1st)) + String(Character.toChars(char2st))
}

fun String?.hideText(replacement: String, visibleCount: Int): String? {
    this ?: return null
    if (length < visibleCount) return this
    val showText = substring(length - visibleCount)
    val hiddenText = substring(0, length - visibleCount).replace("[^.]".toRegex(), replacement)
    return "$hiddenText$showText"
}

private fun cashText(numText: String): String {

    val n = numText.toLong()
    val n100 = n / 100
    val n10 = n / 10 % 10
    val n1 = n % 10

    if (n100 == 0L && n10 == 0L && n1 == 0L) return ""

    val s100 = if (numText.length < 3) "" else when (n100) {
        0L -> "không trăm"
        1L -> "một trăm"
        2L -> "hai trăm"
        3L -> "ba trăm"
        4L -> "bốn trăm"
        5L -> "năm trăm"
        6L -> "sáu trăm"
        7L -> "bảy trăm"
        8L -> "tám trăm"
        else -> "chín trăm"
    }

    val s10 = if (numText.length < 2) "" else when (n10) {
        0L -> if (n1 == 0L) "" else " lẻ"
        1L -> " mười"
        2L -> " hai mươi"
        3L -> " ba mươi"
        4L -> " bốn mươi"
        5L -> " năm mươi"
        6L -> " sáu mươi"
        7L -> " bảy mươi"
        8L -> " tám mươi"
        else -> " chín mươi"
    }

    val s1 = when (n1) {
        0L -> ""
        1L -> if (n10 < 2) " một" else " mốt"
        2L -> " hai"
        3L -> " ba"
        4L -> " bốn"
        5L -> if (n10 == 0L) " năm" else " lăm"
        6L -> " sáu"
        7L -> " bảy"
        8L -> " tám"
        else -> " chín"
    }
    return "$s100$s10$s1"
}