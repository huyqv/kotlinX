package com.huy.library.extension

import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.huy.library.Library

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/11/04
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun Float.dpToPx(): Float {
    val resources = Library.app.resources
    val scale = resources.displayMetrics.density
    return (this * scale + 0.5f)
}

fun Float.pxToDp(): Float {
    return this / (Library.app.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Float.dipToPx(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Library.app.resources.displayMetrics)
}

/**
 * @SnackBar
 */
fun Snackbar.hide() {
    if (isShown) dismiss()
}

fun Snackbar?.isShowing(): Boolean {
    this ?: return false
    return isShown
}

fun View.showSnackBar(msgId: String, action: String, listener: View.OnClickListener?): Snackbar {
    val snackBar = Snackbar.make(this, msgId, Snackbar.LENGTH_INDEFINITE)
    if (listener != null) snackBar.setAction(action, listener)
    snackBar.show()
    return snackBar
}

fun View.showSnackBar(msgId: Int, action: Int, listener: View.OnClickListener?): Snackbar {
    val snackBar = Snackbar.make(this, msgId, Snackbar.LENGTH_INDEFINITE)
    if (listener != null) snackBar.setAction(action, listener)
    snackBar.show()
    return snackBar
}