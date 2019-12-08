package com.huy.library.extension


import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/02/24
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun Activity.fullScreenLayout() {
    try {
        val window = this.window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            this.window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
    } catch (e: NullPointerException) {
        e.printStackTrace()
    }
}

fun Activity.fullScreenWindow() {
    this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}


/**
 * Status bar
 */
fun Activity.statusBarHeight(): Int {
    var result = 0
    val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0)
        result = this.resources.getDimensionPixelSize(resourceId)
    return result
}

fun Activity.statusBarColor(color: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = color
}

fun Activity.statusBarColorRes(@ColorRes res: Int) {
    statusBarColor(color(res))
}

fun Activity.statusBarDrawable(drawable: Drawable?) {
    drawable ?: return
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    if (drawable is ColorDrawable) {
        window.statusBarColor = drawable.color
    } else {
        window.statusBarColor = Color.TRANSPARENT
        window.setBackgroundDrawable(drawable)
    }
}

fun Activity.statusBarDrawable(@DrawableRes res: Int) {
    statusBarDrawable(ContextCompat.getDrawable(this, res))
}

fun Activity.contentUnderStatusBar(view: View) {
    view.setPadding(0, statusBarHeight(), 0, 0)
}


/**
 * Navigation bar
 */
fun Activity.navigationBarColor(color: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.navigationBarColor = color
}

fun Activity.navigationBarColorRes(@ColorRes res: Int) {
    navigationBarColor(color(res))
}

fun Activity.navigationBarDrawable(drawable: Drawable?) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    if (drawable is ColorDrawable) {
        window.navigationBarColor = drawable.color
    } else {
        window.navigationBarColor = Color.TRANSPARENT
        window.setBackgroundDrawable(drawable)
    }
}

fun Activity.navigationBarDrawable(@DrawableRes res: Int) {
    navigationBarDrawable(ContextCompat.getDrawable(this, res))
}

fun Activity.hideNavigationBar() {
    val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window.decorView.systemUiVisibility = flags
        val decorView = window.decorView
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                decorView.systemUiVisibility = flags
            }
        }
    }
}

fun Activity.hideNavigationBar(hasFocus: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus) {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}


/**
 * Keyboard
 */
fun Activity.hideKeyboard() {
    if (currentFocus?.windowToken != null) {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}

fun Activity.showKeyboard() {
    if (currentFocus?.windowToken != null) {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED, 0)
    }
}