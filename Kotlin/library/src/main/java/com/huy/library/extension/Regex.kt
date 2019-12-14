package com.huy.library.extension

import android.text.TextUtils

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/18
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */

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

fun String?.isPersonName(): Boolean {
    if (isNullOrEmpty()) return false
    return this!!.matches("[a-zA-Z ]".toRegex())
}

fun String?.isNotPersonName(): Boolean {
    return !isPersonName()
}

fun String?.isCharacters(): Boolean {
    this ?: return false
    return matches("[a-zA-Z0-9]+".toRegex())
}

fun String?.isNotCharacters(): Boolean {
    return !isCharacters()
}

fun String?.isEmail(): Boolean {
    this ?: return false
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String?.isNotEmail(): Boolean {
    return !isEmail()
}

fun String?.isPhone(): Boolean {
    this ?: return false
    return matches("^[0-9]{16}$".toRegex())
}

fun String?.isNotPhone(): Boolean {
    return !isPhone()
}

fun String?.inRange(min: Int, max: Int): Boolean {
    return this != null && length in min..max
}

fun String?.notInRange(min: Int, max: Int): Boolean {
    return this != null && length !in min..max
}

fun String?.isPassword(): Boolean {
    this ?: return false
    var ch: Char
    var capitalFlag = false
    var lowerCaseFlag = false
    var numberFlag = false
    for (i in 0 until this.length) {
        ch = this[i]
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

fun String?.isNotPassword(): Boolean {
    return !isPassword()
}

fun String?.isDigit(): Boolean {
    if (isNullOrEmpty()) return false
    return TextUtils.isDigitsOnly(this)
}

fun String?.isNotDigit(): Boolean {
    return !isDigit()
}

fun String?.isDate(s: String): Boolean {
    return s.matches(DATE_REGEX.toRegex())
}

