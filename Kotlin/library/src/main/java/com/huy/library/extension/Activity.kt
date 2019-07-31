package com.huy.library.extension


import android.app.Activity
import android.graphics.Color
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
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun Activity.hideKeyboard() {
    if (currentFocus?.windowToken != null) {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}

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

fun Activity.statusBarColor(@ColorRes colorId: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        this.window.statusBarColor = ContextCompat.getColor(this, colorId)
}

fun Activity.statusBarHeight(): Int {
    var result = 0
    val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0)
        result = this.resources.getDimensionPixelSize(resourceId)
    return result
}

fun Activity.setStatusBarBackground(@DrawableRes res: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val background = ContextCompat.getDrawable(this, res)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        window.setBackgroundDrawable(background)
    }
}

fun Activity.contentUnderStatusBar(view: View) {
    view.setPadding(0, statusBarHeight(), 0, 0)
}



