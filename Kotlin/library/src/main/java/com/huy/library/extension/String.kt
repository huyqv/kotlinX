package com.huy.library.extension

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.TextUtils
import android.util.Base64
import android.webkit.MimeTypeMap
import java.net.URI
import java.net.URISyntaxException
import java.security.MessageDigest
import java.text.Normalizer
import java.util.*
import java.util.regex.Pattern

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/18
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun String.toMD5(): String {

    val md = MessageDigest.getInstance("MD5")
    val digested = md.digest(toByteArray())
    return digested.joinToString("") {
        String.format("%02x", it)
    }
}

fun String.toBase64(flags: Int = Base64.DEFAULT): String {
    return Base64.encodeToString(this.toByteArray(), flags)
}

fun String.fromBase64(flags: Int = Base64.DEFAULT): String {
    return String(Base64.decode(this, flags))
}

fun String?.like(string: String?): Boolean {
    if (isNullOrEmpty()) return false
    if (string.isNullOrEmpty()) return false
    val left = this.normalizer() ?: return false
    val right = string.normalizer() ?: return false
    return left.contains(right)
}

fun String?.notNullOrEmpty(): Boolean {
    return !this.isNullOrEmpty()
}

fun String.getMimeType(): String? {

    val extension = MimeTypeMap.getFileExtensionFromUrl(this) ?: return null
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}

fun String.getFileExtension(): String {

    val docIndex = this.lastIndexOf(".") + 1
    if (docIndex == -1) return ""
    return this.substring(docIndex).toLowerCase()
}

fun String.getFileName(): String {

    val docIndex = this.lastIndexOf(".")
    if (docIndex == -1) return this
    return this.substring(0, docIndex)
}

fun String.toTokenList(delim: String = ";"): MutableList<Int> {

    val list: MutableList<Int> = mutableListOf()
    val st = StringTokenizer(this, delim)
    while (st.hasMoreTokens()) list.add(st.nextToken().toInt())
    if (list.isEmpty()) return list
    return list
}

fun String?.red(): String {
    this ?: return ""
    return "<font color=#d12c34><b>$this</b></font>"
}

fun String?.blue(): String {
    this ?: return ""
    return "<font color=#2AA5FF>$this</font>"
}

fun String?.bold(): String {
    this ?: return ""
    return "<b>$this</b>"
}

fun String?.hidden(): String {
    this ?: return ""
    val length = this.length
    if (length < 4) return ""
    return "•••••••${this.substring(length - 2, length)}"
}

fun String?.decodeHtml(): String? {

    if (this.isNullOrEmpty()) return null

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, 1).toString()
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this).toString()
    }
}

fun String?.encodeHtml(): String? {

    if (this.isNullOrEmpty()) return null

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.toHtml(SpannableString(this), 1)
    } else {
        @Suppress("DEPRECATION")
        Html.toHtml(SpannableString(this))
    }
}

fun String?.trimAll(): String? {
    if (this.isNullOrEmpty()) return null
    var temp: String = this
    while (temp.indexOf("  ") != -1) temp = temp.replace("  ", " ")
    if (temp.isEmpty()) return null
    return temp
}

fun String?.capitalize(): String {
    this ?: return ""
    val arr = this.toCharArray()
    var capitalizeNext = true
    var phrase = ""
    for (c in arr) {
        if (capitalizeNext && Character.isLetter(c)) {
            phrase += Character.toUpperCase(c)
            capitalizeNext = false
            continue
        } else if (Character.isWhitespace(c)) {
            capitalizeNext = true
        }
        phrase += c
    }
    return phrase
}

fun String?.normalizer(): String? {
    this ?: return null
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

fun String?.rgbToHex(): String {

    val c = Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)")
    val m = c.matcher(this)
    return if (m.matches()) {
        String.format(
                "#%02x%02x%02x",
                m.group(1).toInt(),
                m.group(2).toInt(),
                m.group(3).toInt()
        )
    } else "#000"
}

/**
 * 123456789012345 -> 1234 4567 8901 2345
 */
fun String?.toCreditNum(): String? {
    return if (TextUtils.isEmpty(this)) null else this!!.replace("\\d{4}".toRegex(), "$0 ")
}

fun String?.toHiddenCreditNum(): String {
    if (this == null || this.length < 17) return "•••• •••• •••• ••••"
    return "•••• •••• •••• ${this.substring(this.lastIndex - 4, this.lastIndex)}"
}

fun String?.sub(start: Int, end: Int, default: String = ""): String {
    if (this.isNullOrEmpty()) return default
    if (end !in 0..this.length) return default
    return this.substring(start, end)
}

fun String.getDomainName(): String {
    return try {
        val uri = URI(this)
        val domain = uri.host ?: return ""
        if (domain.startsWith("www.")) domain.substring(4) else domain
    } catch (e: URISyntaxException) {
        ""
    }
}