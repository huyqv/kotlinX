package com.sample.widget.extension

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.util.Base64
import android.webkit.MimeTypeMap
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.sample.widget.app
import java.net.URI
import java.net.URISyntaxException
import java.security.MessageDigest
import java.text.Normalizer
import java.util.*
import java.util.regex.Pattern

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

fun String.color(@ColorRes colorRes: Int): String {
    val colorInt = ContextCompat.getColor(app, colorRes)
    val hexString = "#${Integer.toHexString(colorInt and 0x00ffffff)}"
    return "<font color=$hexString>$this</font>"
}

fun String.color(hexString: String): String {
    return "<font color=$hexString>$this</font>"
}

fun colorText(string: String, @ColorRes colorRes: Int): String {
    val colorInt = ContextCompat.getColor(app, colorRes)
    val hexString = "#${Integer.toHexString(colorInt and 0x00ffffff)}"
    return "<font color=$hexString>$string</font>"
}

fun String.bold(): String {
    this ?: return ""
    return "<b>$this</b>"
}

fun String.hidden(): String {
    this ?: return ""
    val length = this.length
    if (length < 4) return ""
    return "•••••••${this.substring(length - 2, length)}"
}

fun String.decodeHtml(): String? {

    if (this.isNullOrEmpty()) return null

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, 1).toString()
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this).toString()
    }
}

fun String.encodeHtml(): String? {

    if (this.isNullOrEmpty()) return null

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.toHtml(SpannableString(this), 1)
    } else {
        @Suppress("DEPRECATION")
        Html.toHtml(SpannableString(this))
    }
}

fun String.trimAll(): String? {
    if (this.isNullOrEmpty()) return null
    var temp: String = this
    while (temp.contains("  ")) temp = temp.replace("  ", " ")
    if (temp.isEmpty()) return null
    return temp
}

fun String?.like(s: String?): Boolean {
    val left: String = this.normalizer() ?: return false
    val right: String = s.normalizer() ?: return false
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

fun String?.filter(filterChars: CharArray): String {
    if (this.isNullOrEmpty()) return ""
    var s = ""
    for (i in 0 until lastIndex) {
        val c = get(i).toString()
        if (String(filterChars).contains(c)) {
            s += c
        }
    }
    return s
}

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

fun <T> String?.search(collection: Collection<T>?, block: (T) -> String): Collection<T>? {
    if (collection.isNullOrEmpty()) return null
    if (this.isNullOrEmpty()) return collection
    val s1 = this.normalizer() ?: return null
    return collection.filter {
        block(it).normalizer()?.contains(s1, true) ?: false
    }
}






