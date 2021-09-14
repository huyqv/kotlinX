package com.sample.widget.extension

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.sample.widget.app

fun Drawable?.tint(@ColorInt color: Int): Drawable? {
    this ?: return null
    DrawableCompat.setTint(this, color)
    DrawableCompat.setTintMode(this, PorterDuff.Mode.SRC_IN)
    return this
}

fun Drawable?.tintRes(@ColorRes color: Int): Drawable? {
    return tint(ContextCompat.getColor(app, color))
}