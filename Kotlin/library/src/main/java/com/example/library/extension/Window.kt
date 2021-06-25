package com.example.library.extension

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Insets
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

/**
 * [Activity] extensions
 */
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
            displayMetrics.widthPixels
        }
    }

val Activity.screenRatio: Float
    get() {
        return screenHeight.toFloat() / screenWidth
    }

fun Window.setGravity(gravity: Int = Gravity.CENTER) {
    val wlp = this.attributes ?: return
    @Suppress("DEPRECATION")
    wlp.flags = wlp.flags
    wlp.gravity = Gravity.BOTTOM
    this.attributes = wlp
}

fun Window.setFullScreen() {
    when {
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R -> {
            this.decorView?.windowInsetsController?.hide(
                    WindowInsets.Type.statusBars()
                            or WindowInsets.Type.navigationBars()
            )
        }
        else -> {
            val wlp = this.attributes ?: return
            @Suppress("DEPRECATION")
            wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
            this.attributes = wlp
        }
    }
    setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
}

fun Window.statusBarColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = color
    }
}

fun Window.navBarColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        navigationBarColor = color
    }
}

fun Window.lightStatusBarWidgets() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController?.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
        return
    }
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val flags = decorView.systemUiVisibility
        decorView.systemUiVisibility = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
}

fun Window.lightNavBarWidgets() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController?.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
        return
    }
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val flags = decorView.systemUiVisibility
        decorView.systemUiVisibility = flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
    }
}

fun Window.darkNavBarWidgets() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController?.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
        return
    }
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val flags = decorView.systemUiVisibility
        decorView.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
}

fun Window.darkStatusBarWidgets() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController?.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
        return
    }
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val flags = decorView.systemUiVisibility
        decorView.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

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
    }
    @Suppress("DEPRECATION")
    setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
}

fun Window.windowFullScreen() {
    setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
    )
}

/**
 * Set status bar color
 */
fun Activity.statusBarColor(@ColorInt color: Int) {
    window.statusBarColor(color)
}

fun Fragment.statusBarColor(@ColorInt color: Int) {
    activity?.statusBarColor(color)
}

fun DialogFragment.statusBarColor(@ColorInt color: Int) {
    dialog?.window?.statusBarColor(color)
}

/**
 * Set navigation bar color
 */
fun Activity.navBarColor(@ColorInt color: Int) {
    window.navBarColor(color)
}

fun Fragment.navBarColor(@ColorInt color: Int) {
    activity?.navBarColor(color)
}

fun DialogFragment.navBarColor(@ColorInt color: Int) {
    dialog?.window?.navBarColor(color)
}

/**
 * Set light status bar widgets
 */
fun Activity.lightStatusBarWidgets() {
    window.lightStatusBarWidgets()
}

fun Fragment.lightStatusBarWidgets() {
    activity?.lightStatusBarWidgets()
}

fun DialogFragment.lightStatusBarWidgets() {
    dialog?.window?.lightStatusBarWidgets()
}

/**
 * Set dark status bar widgets
 */
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
 * Set light navigation bar widgets
 */
fun Activity.lightNavBarWidgets() {
    window.lightNavBarWidgets()
}

fun Fragment.lightNavBarWidgets() {
    activity?.lightNavBarWidgets()
}

fun DialogFragment.lightNavBarWidgets() {
    dialog?.window?.lightNavBarWidgets()
}

/**
 * Set dark navigation bar widgets
 */
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
 *  override fun onWindowFocusChanged(hasFocus: Boolean) {
 *      super.onWindowFocusChanged(hasFocus)
 *  }
 */
fun Activity.hideSystemUI(hasFocus: Boolean = true) {
    window.hideSystemUI(hasFocus)
}

fun AlertDialog.hideSystemUI(hasFocus: Boolean = true) {
    window?.hideSystemUI(hasFocus)
}

fun Activity.windowFullScreen() {
    window.windowFullScreen()
}

fun AlertDialog.windowFullScreen() {
    window?.windowFullScreen()
}

fun Activity.windowSafeArea() {
    window.setFlags(
            0,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
    )
}

/**
 * [Fragment] extensions
 */
fun Fragment.windowFullScreen() {
    activity?.windowFullScreen()
}

fun Fragment.windowSafeArea() {
    activity?.windowSafeArea()
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

fun Activity?.statusBarHeight(): Int {
    this ?: return 0
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0)
        result = resources.getDimensionPixelSize(resourceId)
    return result
}

fun Activity?.contentUnderStatusBar(view: View) {
    view.setPadding(0, statusBarHeight(), 0, 0)
}

/**
 * Navigation bar
 */
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