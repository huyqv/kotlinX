package com.huy.kotlin.widget

import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.StyleableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.huy.kotlin.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2020/02/21
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun View.getTypedDrawable(typedArray: TypedArray, @StyleableRes styleId: Int, color: Int): Drawable? {
    val res = typedArray.getResourceId(styleId, 0)
    val drawable = if (res == 0) null else ContextCompat.getDrawable(context, res)
    drawable?.setTintColor(color)
    return drawable
}

fun View.getTypedColor(typedArray: TypedArray, @StyleableRes styleId: Int): Int {
    return typedArray.getInt(styleId, ContextCompat.getColor(context, R.color.colorPrimary))
}

fun View.scaleDrawable(drawable: Drawable?): Drawable? {
    drawable ?: return null
    val bitmap = (drawable as BitmapDrawable).bitmap
    return BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, width, height, true))
}

fun Drawable?.setTintColor(color: Int) {
    this ?: return
    DrawableCompat.setTint(this, color)
    DrawableCompat.setTintMode(this, PorterDuff.Mode.SRC_IN)
}