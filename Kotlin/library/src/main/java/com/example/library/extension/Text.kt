package com.example.library.extension

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
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

val PERSON_NAME = charArrayOf(
        'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M',
        'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' '
)

// Date format dd/MM/yyyy
private val DATE_REGEX = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]|(?:Jan|Mar|May|Jul|Aug|Oct|Dec)))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2]|(?:Jan|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec))\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)(?:0?2|(?:Feb))\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9]|(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep))|(?:1[0-2]|(?:Oct|Nov|Dec)))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$"

// Minimum eight characters, at least one letter and one number
private val PASSWORD_REGEX_1 = Regex("""^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$""")

// Minimum eight characters, at least one letter, one number and one special character:
private val PASSWORD_REGEX_2 = Regex("""^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$""")

// Minimum eight characters, at least one uppercase letter, one lowercase letter and one number
private val PASSWORD_REGEX_3 = Regex("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$""")

// Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character
private val PASSWORD_REGEX_4 = Regex("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$""")

// Minimum eight and maximum 10 characters, at least one uppercase letter, one lowercase letter, one number and one special character:
private val PASSWORD_REGEX_5 = Regex("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,10}$""")

val String?.isEmail: Boolean
    get() {
        this ?: return false
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

val String?.isPhoneNumber: Boolean
    get() {
        val first = this?.firstOrNull() ?: return false
        return first.toString() == "0" && this.length > 9
    }

fun String?.isDate(fmt: SimpleDateFormat): Boolean {
    this ?: return false
    return try {
        val date = fmt.parse(this)
        return date.dateFormat(fmt) == this
    } catch (e: Throwable) {
        false
    }

}

val String?.isPersonName: Boolean
    get() {
        if (isNullOrEmpty()) return false
        return this.matches("[a-zA-Z ]".toRegex())
    }

val String?.isCharacters: Boolean
    get() {
        this ?: return false
        return matches("[a-zA-Z0-9]+".toRegex())
    }

val String?.isPassword: Boolean
    get() {
        this ?: return false
        var ch: Char
        var capitalFlag = false
        var lowerCaseFlag = false
        var numberFlag = false
        for (element in this) {
            ch = element
            when {
                Character.isDigit(ch) -> numberFlag = true
                Character.isUpperCase(ch) -> capitalFlag = true
                Character.isLowerCase(ch) -> lowerCaseFlag = true
            }
            if (numberFlag && capitalFlag && lowerCaseFlag)
                return true
        }
        return false
    }

val String?.isDigit: Boolean
    get() {
        if (isNullOrEmpty()) return false
        return TextUtils.isDigitsOnly(this)
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
