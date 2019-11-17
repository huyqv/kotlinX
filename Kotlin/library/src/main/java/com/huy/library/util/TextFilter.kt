package com.huy.library.util

import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import java.util.regex.Pattern

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/03/31
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object TextFilter {

    val PERSON_NAME = object : InputFilter {

        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {

            var keepOriginal = true
            val sb = StringBuilder(end - start)
            for (i in start until end) {
                val c = source[i]
                if (isCharAllowed(c))
                // put your condition here
                    sb.append(c)
                else
                    keepOriginal = false
            }
            return if (keepOriginal)
                null
            else {
                if (source is Spanned) {
                    val sp = SpannableString(sb)
                    TextUtils.copySpansFrom(source, start, sb.length, null, sp, 0)
                    sp
                } else {
                    sb
                }
            }
        }

        private fun isCharAllowed(c: Char): Boolean {
            val ps = Pattern.compile("^[a-zA-Z ]+$")
            val ms = ps.matcher(c.toString())
            return ms.matches()
        }

    }
}