package com.example.library.extension

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2021/04/28
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun Activity.hideKeyboard() {
    this.findViewById<View>(android.R.id.content)?.windowToken.also { windowToken ->
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun Activity.showKeyboard() {
    (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun Fragment.showKeyboard() {
    activity?.showKeyboard()
}

fun View?.hideKeyboard() {
    this?.post {
        clearFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun View?.showKeyboard() {
    this?.post {
        requestFocus()
        val imm: InputMethodManager? = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun AppCompatActivity.listenKeyboard() {
    val contentView = this.findViewById<View>(android.R.id.content)
    val layoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val rect = Rect()
        contentView.getWindowVisibleDisplayFrame(rect)
        val screenHeight = contentView.rootView.height
        val keypadHeight = screenHeight - rect.bottom
        if (keypadHeight > screenHeight * 0.15) {
            toast("Keyboard is showing")
        } else {
            toast("Keyboard is closed")
        }
    }
    contentView.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)

}