package com.huy.library.extension

import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.widget.EditText
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URI
import java.net.URISyntaxException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.Normalizer
import java.text.NumberFormat
import java.util.*
import java.util.regex.Pattern

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/08
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */

fun String?.normalizer(): String? {
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

fun String?.normalize(): String? {
    this ?: return null
    if (this.isEmpty()) return null
    val s = this.trim { it <= ' ' }
    return Normalizer.normalize(s, Normalizer.Form.NFD)
            .toLowerCase()
            .replace("\\p{M}".toRegex(), "")
            .replace("đ".toRegex(), "d")
}

/**
 * www.oracle.com/download#374 -> oracle.com
 */
fun String.getDomainName(): String {
    return try {
        val uri = URI(this)
        val domain = uri.host ?: return ""
        if (domain.startsWith("www.")) domain.substring(4) else domain
    } catch (e: URISyntaxException) {
        ""
    }
}

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

/**
 * us -> 🇺🇸
 */
fun String?.flagIcon(): String {
    this ?: return ""
    if (length != 2) return ""
    val s = toUpperCase()
    val char1st = Character.codePointAt(s, 0) - 0x41 + 0x1F1E6
    val char2st = Character.codePointAt(s, 1) - 0x41 + 0x1F1E6
    return String(Character.toChars(char1st)) + String(Character.toChars(char2st))
}


/**
 * Format string pattern ex:    423.016024, 9442.456363,    72
 * To                           423,        9,442           72
 */
private val INT_CASH_FORMAT = (NumberFormat.getInstance(Locale.US) as DecimalFormat).also {
    it.applyPattern("#,###,###,###")
}

fun String?.intCash(): String {
    this ?: return ""
    return try {
        var originalString = replace(",", "").replace(".", "")
        if (originalString.contains(",")) originalString = originalString.replace(",".toRegex(), "")
        INT_CASH_FORMAT.format(originalString.toLong())
    } catch (nfe: Exception) {
        ""
    }
}

/**
 * Text watcher to apply pattern: #,###,###,###
 */
fun EditText.addCashWatcher() {
    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
    addTextChangedListener(object : SimpleTextWatcher {
        override fun afterTextChanged(s: Editable?) {
            setSilentText(text.toString().intCash())
        }
    })
}


/**
 * Format string pattern ex:    423.016024, 9442.456363,    72
 * To                           423.01,     9,442.45,       72.0
 */
private val FLOAT_CAST_FORMAT = (NumberFormat.getInstance(Locale.US) as DecimalFormat).also {
    it.applyPattern("#,###,###,###.##")
}

fun String?.floatCash(): String {
    this ?: return ""
    return try {

        if (last().toString() == ".") return this

        val sLength = length
        if (sLength > 1 && substring(sLength - 2, sLength) == ".0") return this
        if (sLength > 2 && substring(sLength - 3, sLength) == ".00") return this

        val docIndex = indexOf(".")
        if (docIndex != -1 && substring(docIndex, length).length > 3) return substring(0, docIndex + 3)

        var originalString = this
        if (originalString.contains(",")) originalString = originalString.replace(",".toRegex(), "")

        val value = originalString.toDouble()
        FLOAT_CAST_FORMAT.format(value)

    } catch (nfe: Exception) {
        ""
    }
}

/**
 * Text watcher to apply pattern: USD #,###,###,###.##
 */
fun EditText.addCashWatcher(maxLength: Int, prefix: String = "") {
    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
    addTextChangedListener(object : SimpleTextWatcher {

        var previousCleanString = ""

        override fun afterTextChanged(s: Editable?) {

            val str = s.toString()
            if (str.length < prefix.length) {
                setText(prefix)
                setSelection(prefix.length)
                return
            }
            if (str == prefix) return
            val cleanString = str.replace(prefix, "").replace("[,]".toRegex(), "")
            if (cleanString == previousCleanString || cleanString.isEmpty()) return
            previousCleanString = cleanString
            val formattedString: String
            formattedString = if (cleanString.contains(".")) cleanString.formatDecimal() else cleanString.formatInteger()
            removeTextChangedListener(this)
            setText(formattedString)
            handleSelection()
            addTextChangedListener(this)
        }

        private fun String?.formatInteger(): String {
            this ?: return ""
            val parsed = BigDecimal(this)
            val formatter = DecimalFormat("$prefix#,###", DecimalFormatSymbols(Locale.US))
            return formatter.format(parsed)
        }

        private fun String?.formatDecimal(): String {
            this ?: return ""
            if (this == ".") return "$prefix."
            val parsed = BigDecimal(this)
            val formatter = DecimalFormat(prefix + "#,###." + getDecimalPattern(), DecimalFormatSymbols(Locale.US))
            formatter.roundingMode = RoundingMode.DOWN
            return formatter.format(parsed)
        }

        private fun String.getDecimalPattern(): String {
            val decimalCount = this.length - this.indexOf(".") - 1
            val decimalPattern = StringBuilder()
            var i = 0
            while (i < decimalCount && i < 2) {
                decimalPattern.append("0")
                i++
            }
            return decimalPattern.toString()
        }

        private fun EditText.handleSelection() {
            setSelection(if (text.length <= maxLength) text.length else maxLength)
        }

    })
}

