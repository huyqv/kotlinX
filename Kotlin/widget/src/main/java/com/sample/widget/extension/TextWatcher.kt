package com.sample.widget.extension

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

abstract class SimpleTextWatcher : TextWatcher {

    private var saveText: String? = null

    open fun EditText.setTextSilently(s: String?) {
        removeTextChangedListener(this@SimpleTextWatcher)
        setText(s)
        handleSelection()
        addTextChangedListener(this@SimpleTextWatcher)
    }

    open fun EditText.handleSelection() {
        setSelection(text.length)
    }

    final override fun afterTextChanged(s: Editable?) {
        if (saveText == s.toString()) return
        afterTextChanged(s.toString())
        saveText = s.toString()
    }

    final override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    final override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    abstract fun afterTextChanged(s: String)
}

fun String.getCashDecimal(prefix: String = ""): BigDecimal {
    return try {
        if (this.isNullOrEmpty()) return BigDecimal.ZERO
        val text = this
            .replace(",", "")
            .replace(" ", "")
            .replace(prefix, "")
        text.toBigDecimal()
    } catch (ignore: Exception) {
        BigDecimal.ZERO
    }
}

fun BigDecimal?.getCashString(prefix: String = ""): String {
    this ?: return " $prefix"
    return "${integerCashFmt.format(this)} $prefix"
}

/**
 *
 */
val integerCashFmt = DecimalFormat("#,###,###,###", DecimalFormatSymbols(Locale.US))

fun String?.integerCash(): String {
    this ?: return ""
    return try {
        var originalString = this.replace(".", "")
        if (originalString.contains(",")) {
            originalString = originalString.replace(",".toRegex(), "")
        }
        val value = originalString.toLong()
        integerCashFmt.format(value)
    } catch (ignore: Exception) {
        ""
    }
}

fun EditText.addIntegerCashWatcher() {
    inputType =
        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
    addTextChangedListener(object : SimpleTextWatcher() {
        override fun afterTextChanged(s: String) {
            setTextSilently(text.toString().integerCash())
        }
    })
}

fun EditText.addIntegerCashWatcher(
    minValue: BigDecimal,
    maxValue: BigDecimal,
    prefix: String = ""
) {
    /*setText(when {
        minValue <= BigDecimal.ZERO -> ""
        else -> "${integerCashFmt.format(minValue)} $prefix"
    })*/
    maxEms = 256
    inputType =
        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
    addTextChangedListener(object : SimpleTextWatcher() {
        override fun afterTextChanged(s: String) {
            val cleanString = s.replace("[^\\.0123456789]".toRegex(), "")

            val value = when {
                cleanString.isNullOrEmpty() -> BigDecimal.ZERO
                else -> try {
                    BigDecimal(cleanString)
                } catch (ignore: NumberFormatException) {
                    BigDecimal.ZERO
                }
            }

            val formattedString = when {
                value == BigDecimal.ZERO -> ""
                value > maxValue -> formatInteger(maxValue)
                else -> formatInteger(value)
            }

            setTextSilently(formattedString)
        }

        override fun EditText.handleSelection() {
            setSelection(text.toString().replace(" $prefix", "").length)
        }

        private fun formatInteger(value: BigDecimal): String {
            return "${integerCashFmt.format(value)} $prefix"
        }
    })
}

/**
 *
 */
val floatCashFormat = DecimalFormat("#,###,###,###.##", DecimalFormatSymbols(Locale.US)).also {
    //it.roundingMode = RoundingMode.DOWN
}

fun String?.floatCash(): String {
    this ?: return ""
    return try {
        if (last().toString() == ".") return this

        val lgt = length
        if (lgt > 1 && substring(lgt - 2, lgt) == ".0") return this
        if (lgt > 2 && substring(lgt - 3, lgt) == ".00") return this

        val docId = indexOf(".")
        if (docId != -1 && substring(docId, length).length > 3) return substring(0, docId + 3)

        var originalString = this
        if (originalString.contains(",")) {
            originalString = originalString.replace(",".toRegex(), "")
        }
        val value = originalString.toDouble()
        floatCashFormat.applyPattern("#,###,###,###.##")
        floatCashFormat.format(value)

    } catch (ignore: Exception) {
        ""
    }
}

fun EditText.addFloatCashWatcher() {
    inputType =
        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
    addTextChangedListener(object : SimpleTextWatcher() {
        override fun afterTextChanged(s: String) {
            setTextSilently(text.toString().floatCash())
        }
    })
}

fun EditText.addFloatCashWatcher(minValue: BigDecimal, maxValue: BigDecimal, prefix: String = "") {
    maxEms = 256
    inputType =
        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
    addTextChangedListener(object : SimpleTextWatcher() {

        override fun afterTextChanged(s: String) {
            val cleanString = s.replace("[^\\.0123456789]".toRegex(), "")

            val value = when {
                cleanString.isNullOrEmpty() -> BigDecimal.ZERO
                else -> try {
                    BigDecimal(cleanString)
                } catch (ignore: NumberFormatException) {
                    BigDecimal.ZERO
                }
            }

            val formattedString = when {
                value == BigDecimal.ZERO -> ""
                value > maxValue -> formatInteger(maxValue)
                cleanString.contains(".") -> formatFloat(value)
                else -> formatInteger(value)
            }

            setTextSilently(formattedString)
        }

        override fun EditText.handleSelection() {
            setSelection(text.toString().replace(" $prefix", "").length)
        }

        private fun formatInteger(value: BigDecimal): String {
            return "${integerCashFmt.format(value)} $prefix"
        }

        private fun formatFloat(value: BigDecimal): String {
            return "${floatCashFormat.format(value)} $prefix"
        }
    })
}

/**
 *
 */
fun EditText.addIntegerQuantityWatcher(
    minValue: BigDecimal,
    maxValue: BigDecimal,
    prefixOne: String = "",
    prefixMany: String = prefixOne
) {
    maxEms = 256
    inputType =
        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
    addTextChangedListener(object : SimpleTextWatcher() {
        override fun afterTextChanged(s: String) {
            val cleanString = s
                .replace(",", "")
                .replace(".", "")
                .replace(" ", "")
                .replace(prefixMany, "")
                .replace(prefixOne, "")

            val cleanDecimal = when {
                cleanString.isNullOrEmpty() -> BigDecimal.ZERO
                else -> BigDecimal(cleanString)
            }
            val formattedString = when {
                cleanDecimal == BigDecimal.ZERO -> ""
                cleanDecimal < minValue -> minValue.formatInteger()
                cleanDecimal > maxValue -> maxValue.formatInteger()
                else -> cleanDecimal.formatInteger()
            }
            setTextSilently(formattedString)
        }

        override fun EditText.handleSelection() {
            setSelection(
                text.toString()
                    .replace(" $prefixMany", "")
                    .replace(" $prefixOne", "")
                    .length
            )
        }

        private fun BigDecimal.formatInteger(): String {
            val prefix = when (this) {
                BigDecimal.ONE -> " $prefixOne"
                else -> " $prefixMany"
            }
            return "${integerCashFmt.format(this)} $prefix"
        }

    })
}

/**
 *
 */
fun EditText.addDateWatcher() {
    inputType = InputType.TYPE_DATETIME_VARIATION_DATE
    addTextChangedListener(object : SimpleTextWatcher() {

        private val sb: StringBuilder = StringBuilder("")

        private var ignore = false

        override fun afterTextChanged(s: String) {
            if (ignore) {
                ignore = false
                return
            }
            sb.clear()
            sb.append(if (s.length > 10) s.subSequence(0, 10) else s)
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

fun EditText.addDateWatcherDMY() {
    setText("DD/MM/YYYY")
    inputType = InputType.TYPE_DATETIME_VARIATION_DATE
    addTextChangedListener(object : SimpleTextWatcher() {

        private var current = ""
        private val ddmmyyyy = "DDMMYYYY"
        private val cal = Calendar.getInstance()

        val editText: EditText get() = this@addDateWatcherDMY

        override fun afterTextChanged(s: String) {
            if (s == current) return
            var clean = s.replace("[^\\d.]|\\.".toRegex(), "")
            val cleanC = current.replace("[^\\d.]|\\.", "")

            val cl = clean.length
            var sel = cl
            var i = 2
            while (i <= cl && i < 6) {
                sel++
                i += 2
            }
            //Fix for pressing delete next to a forward slash
            if (clean == cleanC) sel--

            if (clean.length < 8) {
                clean += ddmmyyyy.substring(clean.length)
            } else {
                //This part makes sure that when we finish entering numbers
                //the date is correct, fixing it otherwise
                var day = Integer.parseInt(clean.substring(0, 2))
                var mon = Integer.parseInt(clean.substring(2, 4))
                var year = Integer.parseInt(clean.substring(4, 8))

                mon = if (mon < 1) 1 else if (mon > 12) 12 else mon
                cal.set(Calendar.MONTH, mon - 1)
                year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                cal.set(Calendar.YEAR, year)
                // ^ first set year for the line below to work correctly
                //with leap years - otherwise, date e.g. 29/02/2012
                //would be automatically corrected to 28/02/2012

                day =
                    if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(Calendar.DATE) else day
                clean = String.format("%02d%02d%02d", day, mon, year)
            }

            clean = String.format(
                "%s/%s/%s", clean.substring(0, 2),
                clean.substring(2, 4),
                clean.substring(4, 8)
            )

            sel = if (sel < 0) 0 else sel
            current = clean
            editText.setText(current)
            editText.setSelection(if (sel < current.count()) sel else current.count())
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

fun TextView.afterTextChanged(block: (String) -> Unit) {
    addTextChangedListener(object : SimpleTextWatcher() {
        override fun afterTextChanged(s: String) {
            block(s)
        }
    })
}

fun TextView.afterTextChanged(delayInterval: Long, block: (String) -> Unit) {
    addTextChangedListener(object : SimpleTextWatcher() {
        override fun afterTextChanged(s: String) {
            lifecycleScope?.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) { delay(delayInterval) }
                block(s)
            }
        }
    })
}