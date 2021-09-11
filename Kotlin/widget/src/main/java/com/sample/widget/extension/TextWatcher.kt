package com.sample.widget.extension

import android.text.Editable
import android.text.InputType
import android.widget.EditText
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

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
        if (docIndex != -1 && substring(docIndex, length).length > 3) return substring(
                0,
                docIndex + 3
        )

        var originalString = this
        if (originalString.contains(",")) originalString = originalString.replace(",".toRegex(), "")

        val value = originalString.toDouble()
        FLOAT_CAST_FORMAT.format(value)
    } catch (nfe: Exception) {
        ""
    }
}

/**
 * Text watcher to apply pattern: #,###,###,###
 */
fun EditText.addCashWatcher() {
    inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
    addTextChangedListener(object : SimpleTextWatcher {
        override fun afterTextChanged(s: Editable?) {
            setTextSilently(text.toString().intCash())
        }
    })
}

val EditText.amount: BigDecimal
    get() {
        return try {
            val s = this.text?.toString()
            if (s.isNullOrEmpty()) return BigDecimal.ZERO
            val text = s.toString().replace(",", "")
            text.toBigDecimal()
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
    }

/**
 * Text watcher to apply pattern: USD #,###,###,###.##
 */
fun EditText.addCashWatcher(maxLength: Int, prefix: String = "") {
    inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
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
            formattedString =
                    if (cleanString.contains(".")) cleanString.formatDecimal() else cleanString.formatInteger()
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
            val formatter = DecimalFormat(
                    prefix + "#,###." + getDecimalPattern(),
                    DecimalFormatSymbols(Locale.US)
            )
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

fun EditText.addDateWatcher() {
    addTextChangedListener(object : SimpleTextWatcher {

        private val sb: StringBuilder = StringBuilder("")

        private var ignore = false

        override fun afterTextChanged(s: Editable?) {
            if (ignore) {
                ignore = false
                return
            }

            sb.clear()
            sb.append(
                    if (s!!.length > 10) {
                        s.subSequence(0, 10)
                    } else {
                        s
                    }
            )

            if (sb.lastIndex == 2) {
                if (sb[2] != '/') {
                    sb.insert(2, "/")
                }
            } else if (sb.lastIndex == 5) {
                if (sb[5] != '/') {
                    sb.insert(5, "/")
                }
            }
            ignore = true
            this@addDateWatcher.setText(sb.toString())
            this@addDateWatcher.setSelection(sb.length)
        }
    })
}