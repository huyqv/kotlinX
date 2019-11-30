package com.huy.library.util

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt

/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: Kotlin
 * @Created: Huy QV 2019/11/26
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object Dynamic {

    fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().displayMetrics).roundToInt()
    }

    fun pxToDp(pixels: Int): Int {
        return (pixels / Resources.getSystem().displayMetrics.density).roundToInt()
    }

    fun spToPx(sp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().displayMetrics).roundToInt()
    }

    fun pxToSp(pixels: Int): Int {
        return (pixels / Resources.getSystem().displayMetrics.density).roundToInt()
    }

    fun dpToSp(dp: Float): Int {
        return (dpToPx(dp) / spToPx(dp).toFloat()).roundToInt()
    }

}
