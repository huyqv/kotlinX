package com.example.library.extension


import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.WindowInsets
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
fun <T : Activity> Activity.start(cls: Class<T>) {
    startActivity(Intent(this, cls))
}

fun <T : Activity> Activity.startFinish(cls: Class<T>) {
    startActivity(Intent(this, cls))
    finish()
}

fun <T : Activity> Activity.startClear(cls: Class<T>) {
    startActivity(Intent(this, cls))
    finish()
}

/**
 * Status bar
 */
fun Activity?.hideStatusBar() {
    this ?: return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.hide(WindowInsets.Type.statusBars())
    } else {
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}

fun Activity?.lightStatusBar() {
    this ?: return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = 8192
    }


}

fun Activity?.darkStatusBar() {
    this ?: return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = 0
    }
}

fun Activity?.statusBarHeight(): Int {
    this ?: return 0
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0)
        result = resources.getDimensionPixelSize(resourceId)
    return result
}

fun Activity?.statusBarColor(color: Int?) {
    color ?: return
    this ?: return
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = color
    if (color.isDarkColor()) darkStatusBar() else lightStatusBar()
}

fun Activity?.statusBarColorRes(@ColorRes res: Int) {
    this ?: return
    statusBarDrawable(res)
}

fun Activity?.statusBarDrawable(drawable: Drawable?) {
    this ?: return
    drawable ?: return
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = Color.TRANSPARENT
    window.setBackgroundDrawable(drawable)
    (drawable as? ColorDrawable)?.also {
        if (it.color.isDarkColor()) {
            darkStatusBar()
        } else {
            lightStatusBar()
        }
    }
}

fun Activity?.statusBarDrawable(@DrawableRes res: Int) {
    this ?: return
    statusBarDrawable(ContextCompat.getDrawable(this, res))
}

fun Activity?.contentUnderStatusBar(view: View) {
    view.setPadding(0, statusBarHeight(), 0, 0)
}

/**
 * Navigation bar
 */
fun Activity?.navigationBarColor(color: Int) {
    this ?: return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = color
    }
}

fun Activity?.navigationBarColorRes(@ColorRes res: Int) {
    this ?: return
    navigationBarDrawable(ContextCompat.getDrawable(this, res))
}

fun Activity?.navigationBarDrawable(drawable: Drawable?) {
    this ?: return
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    if (drawable is ColorDrawable) {
        window.navigationBarColor = drawable.color
    } else {
        window.navigationBarColor = Color.TRANSPARENT
        window.setBackgroundDrawable(drawable)
    }
}

fun Activity?.navigationBarDrawable(@DrawableRes res: Int) {
    this ?: return
    navigationBarDrawable(ContextCompat.getDrawable(this, res))
}

fun Activity?.hideNavigationBar(hasFocus: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && hasFocus) this?.window?.apply {
        setDecorFitsSystemWindows(false)
        return
    }
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus) {
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        val decorView = this?.window?.decorView ?: return
        decorView.systemUiVisibility = flags
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                decorView.systemUiVisibility = flags
            }
        }
    }
}

/**
 * Keyboard
 */
fun Activity?.hideKeyboard() {
    this?.currentFocus?.windowToken?.also {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it, 0)
    }
}

fun Activity.showKeyboard() {
    if (currentFocus?.windowToken != null) {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED, 0)
    }
}

/**
 * Orientation
 */
fun Activity.lockLandscape() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

fun Activity.lockPortrait() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

fun Activity.unlockOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}

