package com.example.library.extension

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.util.Base64
import android.webkit.MimeTypeMap
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.example.library.Library
import java.security.MessageDigest
import java.util.*
import java.util.regex.Pattern

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/18
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */

fun String?.notNullOrEmpty(): Boolean {
    return !this.isNullOrEmpty()
}

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

fun String?.color(@ColorRes color: Int): String {
    return color("#${Integer.toHexString(ContextCompat.getColor(Library.app, color) and 0x00ffffff)}")
}

fun String?.color(color: String): String {
    this ?: return ""
    return "<font color=$color>$this</font>"
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
    while (temp.contains("  ")) temp = temp.replace("  ", " ")
    if (temp.isEmpty()) return null
    return temp
}

fun String?.like(s: String?): Boolean {
    val left = this.normalizer() ?: return false
    val right = s.normalizer() ?: return false
    return left.contains(right) || right.contains(left)
}

fun String?.rgbToHex(): String {
    this ?: return "#000"
    val c = Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)")
    val m = c.matcher(this)
    return if (m.matches()) {
        String.format(
                "#%02x%02x%02x",
                m.group(1)?.toInt(),
                m.group(2)?.toInt(),
                m.group(3)?.toInt()
        )
    } else "#000"
}

fun String.tag(): String {
    return if (length > 23) substring(0, 22) else this
}

fun String?.sub(start: Int, end: Int, default: String = ""): String {
    if (this.isNullOrEmpty()) return default
    if (end !in 0..this.length) return default
    return this.substring(start, end)
}







