package com.sample.library.extension

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Insets
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

val Activity.screenWidth: Int
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics: WindowMetrics = this.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            this.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

val Activity.screenHeight: Int
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics: WindowMetrics = this.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            this.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }

val Activity.screenRatio: Float
    get() {
        return screenHeight.toFloat() / screenWidth
    }

/**
 * Status bar
 */
fun Window.statusBarColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = color
    }
}

fun Activity.statusBarColor(@ColorInt color: Int) {
    window.statusBarColor(color)
}

fun Fragment.statusBarColor(@ColorInt color: Int) {
    activity?.statusBarColor(color)
}

fun DialogFragment.statusBarColor(@ColorInt color: Int) {
    dialog?.window?.statusBarColor(color)
}

fun Window.lightStatusBarWidgets() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
        return
    }
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var flags = decorView.systemUiVisibility
        decorView.systemUiVisibility = flags xor
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR xor
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
}

fun Activity.lightStatusBarWidgets() {
    window.lightStatusBarWidgets()
}

fun Fragment.lightStatusBarWidgets() {
    activity?.lightStatusBarWidgets()
}

fun DialogFragment.lightStatusBarWidgets() {
    dialog?.window?.lightStatusBarWidgets()
}

fun Window.darkStatusBarWidgets() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
        return
    }
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val flags = decorView.systemUiVisibility
        decorView.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

fun Activity.darkStatusBarWidgets() {
    window.darkStatusBarWidgets()
}

fun Fragment.darkStatusBarWidgets() {
    activity?.darkStatusBarWidgets()
}

fun DialogFragment.darkStatusBarWidgets() {
    dialog?.window?.darkStatusBarWidgets()
}

/**
 * Navigation bar
 */
fun Window.navBarColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        navigationBarColor = color
    }
}

fun Activity.navBarColor(@ColorInt color: Int) {
    window.navBarColor(color)
}

fun Fragment.navBarColor(@ColorInt color: Int) {
    activity?.navBarColor(color)
}

fun DialogFragment.navBarColor(@ColorInt color: Int) {
    dialog?.window?.navBarColor(color)
}

fun Window.lightNavBarWidgets() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
        )
        return
    }
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val flags = decorView.systemUiVisibility
        decorView.systemUiVisibility = flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
    }
}

fun Activity.lightNavBarWidgets() {
    window.lightNavBarWidgets()
}

fun Fragment.lightNavBarWidgets() {
    activity?.lightNavBarWidgets()
}

fun DialogFragment.lightNavBarWidgets() {
    dialog?.window?.lightNavBarWidgets()
}

fun Window.darkNavBarWidgets() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
        )
        return
    }
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val flags = decorView.systemUiVisibility
        decorView.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
}

fun Activity.darkNavBarWidgets() {
    window?.darkNavBarWidgets()
}

fun Fragment.darkNavBarWidgets() {
    activity?.darkNavBarWidgets()
}

fun DialogFragment.darkNavBarWidgets() {
    dialog?.window?.darkNavBarWidgets()
}

/**
 *
 */
fun Window.lightSystemWidgets() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
        insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
        )
        return
    }
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var flags = decorView.systemUiVisibility
        decorView.systemUiVisibility = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

fun Activity.lightSystemWidgets() {
    window.lightSystemWidgets()
}

fun Fragment.lightSystemWidgets() {
    activity?.lightSystemWidgets()
}

fun DialogFragment.lightSystemWidgets() {
    dialog?.window?.lightSystemWidgets()
}

fun Window.darkSystemWidgets() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
        insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
        )
        return
    }
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val flags = decorView.systemUiVisibility
        decorView.systemUiVisibility = flags or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
}

fun Activity.darkSystemWidgets() {
    window.darkSystemWidgets()
}

fun Fragment.darkSystemWidgets() {
    activity?.darkSystemWidgets()
}

fun DialogFragment.darkSystemWidgets() {
    dialog?.window?.darkSystemWidgets()
}

/**
 * Fullscreen
 */
fun Window.windowFullScreen() {
    setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}

fun Activity.windowFullScreen() {
    window.windowFullScreen()
}

fun Fragment.windowFullScreen() {
    activity?.windowFullScreen()
}

fun Window.windowSafeArea() {
    setFlags(0, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}

fun Activity.windowSafeArea() {
    window.windowSafeArea()
}

fun Fragment.windowSafeArea() {
    activity?.windowSafeArea()
}

/**
 * system ui visible
 */
fun Window.hideSystemUI(hasFocus: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && hasFocus) {
        insetsController?.hide(WindowInsets.Type.statusBars())
        setDecorFitsSystemWindows(false)
        return
    }
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus) {
        val flags = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.systemUiVisibility = flags
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                decorView.systemUiVisibility = flags
            }
        }
        return
    }
    @Suppress("DEPRECATION")
    setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
}

fun Activity.hideSystemUI(hasFocus: Boolean = true) {
    window.hideSystemUI(hasFocus)
}

fun AlertDialog.hideSystemUI(hasFocus: Boolean = true) {
    window?.hideSystemUI(hasFocus)
}

/**
 * Activity orientation
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

/**
 * Window drawable
 */
fun Window.windowDrawable(drawable: Drawable?) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    setBackgroundDrawable(drawable)
}

fun Activity.windowDrawable(drawable: Drawable?) {
    window.windowDrawable(drawable)
}

fun Fragment.windowDrawable(drawable: Drawable?) {
    activity?.windowDrawable(drawable)
}