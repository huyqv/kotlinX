package com.example.library.extension

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentActivity
import com.example.library.Library
import kotlin.math.roundToInt


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/11/04
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
private val app: Application get() = Library.app

fun Float.dpToPx(): Float {
    val resources = app.resources
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
    return this / (app.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Float.dipToPx(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, app.resources.displayMetrics)
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

val View.activity: Activity? get() = context as? Activity

val View.fragmentActivity: FragmentActivity? get() = context as? FragmentActivity

val View?.backgroundColor: Int
    get() {
        this ?: return Color.WHITE
        this.background ?: return Color.WHITE
        var color = Color.TRANSPARENT
        val background: Drawable = this.background
        if (background is ColorDrawable) color = background.color
        return color
    }

/**
 * [View] visible state
 */
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

fun View?.post(delayed: Long, runnable: Runnable) {
    this?.postDelayed(runnable, delayed)
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

fun View.backgroundTint(@ColorInt color: Int) {
    post {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            background?.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_OVER)
        } else {
            @Suppress("DEPRECATION")
            background?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}

fun View.backgroundTintRes(@ColorRes colorRes: Int) {
    backgroundTint(color(colorRes))
}

fun View.getBitmap(w: Int = width, h: Int = height, block: (Bitmap?) -> Unit) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
            this@getBitmap.removeOnLayoutChangeListener(this)
            v.post {
                val bitmap = getBitmap(w, h)
                block(bitmap)
            }
        }
    })
}

fun View.getBitmap(w: Int = width, h: Int = height): Bitmap? {
    return try {
        if (w > 0 && h > 0) {
            this.measure(View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.EXACTLY))
        }
        this.layout(0, 0, this.measuredWidth, this.measuredHeight)
        val bitmap = Bitmap.createBitmap(this.measuredWidth, this.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.background?.draw(canvas)
        this.draw(canvas)
        bitmap
    } catch (ignore: Exception) {
        null
    }
}

fun View.setRatio(width: Int, height: Int) {
    ConstraintSet().also {
        it.clone(parent as ConstraintLayout)
        val ratio = if (layoutParams.width == 0) "w,$width:$height"
        else "w,$height:$width"
        it.setDimensionRatio(this.id, ratio)
        it.applyTo(parent as ConstraintLayout)
    }
}

fun View.getSize(block: (Int /*width*/, Int/*height*/) -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            block(width, height)
        }
    })
}

/**
 * Inflate
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ViewGroup.enableChildren(enabled: Boolean) {
    val childCount = this.childCount
    for (i in 0 until childCount) {
        val view = this.getChildAt(i)
        view.isEnabled = enabled
        if (view is ViewGroup) {
            view.enableChildren(enabled)
        }
    }
}

fun inflater(@LayoutRes layoutRes: Int): View {
    val inflater = app.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    return inflater.inflate(layoutRes, null)
}
