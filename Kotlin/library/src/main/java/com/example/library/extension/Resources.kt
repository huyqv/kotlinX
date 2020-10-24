package com.example.library.extension

import android.app.Application
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.library.Library

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/04/01
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
private val app: Application get() = Library.app

fun anim(@AnimRes res: Int): Animation {
    return AnimationUtils.loadAnimation(app, res)
}

fun drawable(@DrawableRes res: Int): Drawable {
    return ContextCompat.getDrawable(app, res)!!
}

fun createDrawable(@DrawableRes res: Int): Drawable? {
    return drawable(res).constantState?.newDrawable()?.mutate()
}

fun Drawable?.tint(@ColorInt color: Int): Drawable? {
    this ?: return null
    DrawableCompat.setTint(this, color)
    DrawableCompat.setTintMode(this, PorterDuff.Mode.SRC_IN)
    return this
}

fun color(@ColorRes res: Int): Int {
    return ContextCompat.getColor(app, res)
}

fun string(@StringRes res: Int): String {
    return app.getString(res)
}

fun string(@StringRes res: Int, vararg args: Any?): String {
    return try {
        String.format(app.getString(res), *args)
    } catch (ignore: Exception) {
        ""
    }
}

fun pixel(@DimenRes res: Int): Float {
    return app.resources.getDimension(res)
}
