package com.sample.widget.extension

import android.graphics.drawable.Drawable
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.sample.widget.app

fun anim(@AnimRes res: Int): Animation {
    return AnimationUtils.loadAnimation(app, res)
}

fun drawable(@DrawableRes res: Int): Drawable {
    return ContextCompat.getDrawable(app, res)!!
}

fun createDrawable(@DrawableRes res: Int): Drawable? {
    return drawable(res).constantState?.newDrawable()?.mutate()
}

fun pixels(@DimenRes res: Int): Float {
    return app.resources.getDimensionPixelSize(res).toFloat()
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

