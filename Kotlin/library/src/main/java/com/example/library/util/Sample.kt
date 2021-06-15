package com.example.library.util

import android.graphics.Color
import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import androidx.annotation.IntDef
import java.util.regex.Pattern

object Sample {

    object RandomColor {

        private var colors = getColors(Color.BLACK)

        fun next(): Int {
            val color = colors.random()
            colors.remove(color)
            if (colors.isEmpty()) colors = getColors(color)
            return color
        }

        private fun getColors(color: Int): MutableList<Int> {
            val colors = mutableListOf(
                    Color.BLACK,
                    Color.RED,
                    Color.BLUE,
                    Color.GREEN,
                    Color.YELLOW,
                    Color.MAGENTA,
                    Color.CYAN
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