package com.huy.library.extension

import android.text.Editable
import android.widget.EditText
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/08
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
private val decimalFormat = DecimalFormat("#,###,###,###")

fun String?.moneyFormat(/* format #,###,###,### */): String {
    this ?: return ""
    return try {
        var originalString = this.replace(".", "")
        if (originalString.contains(",")) originalString = originalString.replace(",".toRegex(), "")
        decimalFormat.format(originalString.toLong())
    } catch (nfe: Exception) {
        ""
    }
}


private val decimalFormat2 = DecimalFormat("#,###,###,###.##")

fun String?.moneyFormat2(/* format #,###,###,###.## */): String {
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
        decimalFormat2.format(value)

    } catch (nfe: Exception) {
        ""
    }
}


class CashTextWatcher(editText: EditText) : SimpleTextWatcher {

    private val viewReference: WeakReference<EditText> = WeakReference(editText)

    private val editText: EditText? get() = viewReference.get()

    override fun afterTextChanged(s: Editable?) {
        editText?.apply {
            val s = text.toString().moneyFormat()
            removeTextChangedListener(this@CashTextWatcher)
            setText(s)
            setSelection(s.length)
            addTextChangedListener(this@CashTextWatcher)
        }
    }

}

class CashTextWatcher2(editText: EditText, private val prefix: String = "") : SimpleTextWatcher {

    private val maxLength = 20

    private var previousCleanString: String = ""

    private val editTextWeakReference: WeakReference<EditText> = WeakReference(editText)

    private val editText: EditText? get() = editTextWeakReference.get()

    override fun afterTextChanged(s: Editable?) {
        editText?.apply {
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
            removeTextChangedListener(this@CashTextWatcher2)
            setText(formattedString)
            handleSelection()
            addTextChangedListener(this@CashTextWatcher2)
        }
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

}
