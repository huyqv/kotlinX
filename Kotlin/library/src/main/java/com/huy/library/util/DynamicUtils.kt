package com.huy.library.util

import android.content.res.Resources
import android.util.TypedValue

/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: Kotlin
 * @Created: Huy QV 2019/11/26
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */

object DynamicUtils {

    fun convertDpToPixels(dp: Float): Int {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, Resources.getSystem().displayMetrics))
    }

    fun convertPixelsToDp(pixels: Int): Int {
        return Math.round(pixels / Resources.getSystem().displayMetrics.density)
    }

    fun convertSpToPixels(sp: Float): Int {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, Resources.getSystem().displayMetrics))
    }

    fun convertPixelsToSp(pixels: Int): Int {
        return Math.round(pixels / Resources.getSystem().displayMetrics.density)
    }

    fun convertDpToSp(dp: Float): Int {
        return Math.round(convertDpToPixels(dp) / convertSpToPixels(dp).toFloat())
    }

}
