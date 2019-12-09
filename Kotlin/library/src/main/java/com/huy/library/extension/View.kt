package com.huy.library.extension

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.huy.library.Library
import kotlin.math.roundToInt


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

fun Float.spToPx(): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics).roundToInt()
}

fun Float.dpToSp(): Int {
    return (this.dpToPx() / this.spToPx().toFloat()).roundToInt()
}

fun Float.pxToDp(): Float {
    return this / (Library.app.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Float.dipToPx(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Library.app.resources.displayMetrics)
}

fun Int.pxToDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).roundToInt()
}

fun Int.pxToSp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).roundToInt()
}

fun Int.isDarkColor(): Boolean {
    val darkness = 1 - (0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this)) / 255
    return darkness >= 0.5
}

fun Int.isLightColor(): Boolean {
    return !isDarkColor()
}

fun Int.isDarkColorRes(): Boolean {
    return color(this).isDarkColor()
}

fun Int.isLightColorRes(): Boolean {
    return color(this).isLightColor()
}


/**
 * View
 */
val WRAP = -2

val MATCH = -1

fun show(vararg views: View) {
    for (v in views) v.show()
}

fun hide(vararg views: View) {
    for (v in views) v.hide()
}

fun gone(vararg views: View) {
    for (v in views) v.gone()
}

fun View?.activity(): Activity? {
    return this?.context as? Activity
}

fun View.fragmentActivity(): FragmentActivity? {
    return context as? FragmentActivity
}

fun View.preventAction(time: Long = 1200) {
    isEnabled = false
    postDelayed({ isEnabled = true }, time)
}

fun View.preventClick(time: Long = 1200) {
    isClickable = false
    postDelayed({ isClickable = true }, time)
}

fun View.show() {
    if (visibility != View.VISIBLE) visibility = View.VISIBLE
}

fun View.isShow(show: Boolean?) {
    visibility = if (show == true) View.VISIBLE
    else View.INVISIBLE
}

fun View.hide() {
    if (visibility != View.INVISIBLE) visibility = View.INVISIBLE
}

fun View.isHide(hide: Boolean?) {
    visibility = if (hide == true) View.INVISIBLE
    else View.VISIBLE
}

fun View.gone() {
    if (visibility != View.GONE) visibility = View.GONE
}

fun View.isGone(gone: Boolean?) {
    visibility = if (gone == true) View.GONE
    else View.VISIBLE
}

fun View.updateStatusBar() {
    (context as? Activity)?.statusBarDrawable(background)
}


/**
 * @param animationStyle animationStyle
 * <style name="PopupStyle">
 *      <item name="android:windowEnterAnimation">@anim/anim1</item>
 * </style>
 */
fun View.showPopup(@LayoutRes layoutRes: Int, @StyleRes animationStyle: Int, block: (View, PopupWindow) -> Unit) {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val v = inflater.inflate(layoutRes, null)
    val popup = PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
    popup.animationStyle = animationStyle
    popup.showAsDropDown(this)
    block(v, popup)
}

fun View.backgroundTint(@ColorRes res: Int) {
    post {
        background.setColorFilter(ContextCompat.getColor(context, res), PorterDuff.Mode.SRC_ATOP)
    }
}


/**
 * SnackBar
 */
fun View.showSnackBar(s: String, action: String, listener: View.OnClickListener?): Snackbar {
    val snackBar = Snackbar.make(this, s, Snackbar.LENGTH_INDEFINITE)
    if (listener != null) snackBar.setAction(action, listener)
    snackBar.show()
    return snackBar
}

fun View.showSnackBar(@StringRes res: Int, action: String, listener: View.OnClickListener?): Snackbar {
    return this.showSnackBar(string(res), action, listener)
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


/**
 * Inflate
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun inflater(@LayoutRes layoutRes: Int): View {
    val inflater = Library.app.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    return inflater.inflate(layoutRes, null)
}
