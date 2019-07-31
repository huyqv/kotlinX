package com.huy.library.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/10/02
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class WrappedTextView : AppCompatTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        val editable = editableText ?: return
        val width = width - paddingLeft - paddingRight

        if (width == 0) return


        val p = paint
        val widths = FloatArray(editable.length)
        p.getTextWidths(editable.toString(), widths)
        var curWidth = 0.0f
        var lastWSPos = -1
        var strPos = 0
        val newLine = '\n'
        val newLineStr = "\n"
        var reset = false
        var insertCount = 0

        /*
         * Traverse the string from the start position, adding each character's width to the total
         * until: 1) A whitespace character is found. In this case, mark the whitespace position. If
         * the width goes over the max, this is where the newline will be inserted. 2) A newline
         * character is found. This resets the curWidth counter. curWidth > width. Replace the
         * whitespace with a newline and reset the counter.
         */
        while (strPos < editable.length) {
            curWidth += widths[strPos]

            val curChar = editable[strPos]

            if (curChar == newLine) {
                reset = true
            } else if (Character.isWhitespace(curChar)) {
                lastWSPos = strPos
            } else if (curWidth > width && lastWSPos >= 0) {
                editable.replace(lastWSPos, lastWSPos + 1, newLineStr)
                insertCount++
                strPos = lastWSPos
                lastWSPos = -1
                reset = true
            }
            if (reset) {
                curWidth = 0.0f
                reset = false
            }
            strPos++
        }

        if (insertCount != 0) {
            text = editable
        }
    }

}