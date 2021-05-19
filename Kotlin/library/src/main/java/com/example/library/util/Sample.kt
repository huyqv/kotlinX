package com.example.library.util

import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import androidx.annotation.IntDef
import com.example.library.R
import java.util.regex.Pattern

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2021/05/16
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object Sample {

    object RandomColor {

        private var colors = getColors(R.color.colorTurquoise)

        fun next(): Int {
            val color = colors.random()
            colors.remove(color)
            if (colors.isEmpty()) colors = getColors(color)
            return color
        }

        private fun getColors(color: Int): MutableList<Int> {
            val colors = mutableListOf(
                    R.color.colorTurquoise,
                    R.color.colorPeterRiver,
                    R.color.colorPink,
                    R.color.colorGold,
                    R.color.colorCarrot,
                    R.color.colorIndianRed,
                    R.color.colorGrey,
                    R.color.colorPrimary
            )
            colors.remove(color)
            return colors
        }
    }

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

    internal object InDefExample {
        const val OPTION1 = 1
        const val OPTION2 = 2

        @IntDef(OPTION1, OPTION2)
        private annotation class Option
    }
}