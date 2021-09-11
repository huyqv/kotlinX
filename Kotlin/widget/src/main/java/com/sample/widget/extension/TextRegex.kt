package com.sample.widget.extension

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

class RegexBuilder {
    private var str = ""

    fun leastOnce(s: String) {
        str += """(?=.*[$s])"""
    }

    fun lengthIn(min: Int, max: Int) {
        str += """.{$min,$max}"""
    }

    fun notAllow(s: String) {
        str += """[^$s]"""
    }

    fun allow(s: String) {
        str += """[$s]+"""
    }

    fun build(): Regex {
        return str.toRegex()
    }
}

val passwordRex = RegexBuilder().apply {
    leastOnce("a-z")
    leastOnce("A-Z")
    leastOnce("0-9")
    leastOnce("@\$!%*#?&")
    lengthIn(6,24)
}.build()

val DIGIT_REGEX
    get() = """[0-9]+""".toRegex()

val USERNAME_REGEX
    get() = """[0-9a-zA-Z]+""".toRegex()

val PERSON_NAME_REGEX
    get() = """[a-zA-Z ]+[a-zA-Z]+""".toRegex()

private val VN_CHARS get() = "a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ"

val VN_PERSON_NAME_REGEX
    get() = """[$VN_CHARS ]+[$VN_CHARS]+""".toRegex()

val DIGIT_CHARS
    get() = charsFilter(charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'))

val USERNAME_CHARS
    get() = charArrayOf(
            'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J',
            'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M',
            'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j',
            'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    )

val PERSON_NAME_CHARS
    get() = charArrayOf(
            'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J',
            'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M',
            'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j',
            'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '-'
    )

val VN_PERSON_NAME_CHARS
    get() = charArrayOf(
            'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J',
            'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M',
            'À', 'Á', 'Ả', 'Ã', 'Ạ',
            'Ă', 'Ằ', 'Ắ', 'Ẳ', 'Ẵ', 'Ặ',
            'Â', 'Ầ', 'Ấ', 'Ẩ', 'Ẫ', 'Ậ',
            'È', 'É', 'Ẻ', 'Ẽ', 'Ẹ',
            'Ề', 'Ế', 'Ể', 'Ễ', 'Ệ',
            'Ì', 'Í', 'Ỉ', 'Ĩ', 'Ị',
            'Ò', 'Ó', 'Ỏ', 'Õ', 'Ọ',
            'Ơ', 'Ờ', 'Ớ', 'Ở', 'Ỡ', 'Ợ',
            'Ô', 'Ồ', 'Ố', 'Ổ', 'Ỗ', 'Ộ',
            'Ù', 'Ú', 'Ủ', 'Ũ', 'Ụ',
            'Ư', 'Ừ', 'Ứ', 'Ử', 'Ữ', 'Ự',
            'Đ',
            'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j',
            'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm',
            'à', 'á', 'ả', 'ã', 'ạ',
            'ă', 'ằ', 'ắ', 'ẳ', 'ẵ', 'ặ',
            'â', 'ầ', 'ấ', 'ẩ', 'ẫ', 'ậ',
            'è', 'é', 'ẻ', 'ẽ', 'ẹ',
            'ề', 'ế', 'ể', 'ễ', 'ệ',
            'ì', 'í', 'ỉ', 'ĩ', 'ị',
            'ò', 'ó', 'ỏ', 'õ', 'ọ',
            'ơ', 'ờ', 'ớ', 'ở', 'ỡ', 'ợ',
            'ô', 'ồ', 'ố', 'ổ', 'ỗ', 'ộ',
            'ù', 'ú', 'ủ', 'ũ', 'ụ',
            'ư', 'ừ', 'ứ', 'ử', 'ữ', 'ự',
            'đ',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '-'
    )

val String?.isPersonName: Boolean
    get() {
        if (isNullOrEmpty()) return false
        return this.matches("[0-9a-zA-Z ]".toRegex())
    }

val String?.isUsername: Boolean
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

val String?.isEmail: Boolean
    get() {
        this ?: return false
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

val String?.isNotEmail: Boolean
    get() = !isEmail

val String?.isPhoneNumber: Boolean
    get() {
        val first = this?.firstOrNull() ?: return false
        return first.toString() == "0" && this.length > 9
    }

fun String?.isDate(fmt: SimpleDateFormat): Boolean {
    this ?: return false
    return try {
        val date: Date = fmt.parse(this)
        return fmt.format(date) == this
    } catch (e: Throwable) {
        false
    }
}