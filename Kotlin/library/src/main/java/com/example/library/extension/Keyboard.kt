package com.example.library.extension

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

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
    view?.hideKeyboard()
}

fun Fragment.showKeyboard() {
    view?.showKeyboard()
}

private var keypadHeight: Int = 0

fun AppCompatActivity.listenKeyboard() {
    val contentView = this.findViewById<View>(android.R.id.content)
    val layoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val rect = Rect()
        contentView.getWindowVisibleDisplayFrame(rect)
        val screenHeight = contentView.rootView.height
        val currentKeypadHeight = screenHeight - rect.bottom
        if (keypadHeight == currentKeypadHeight) return@OnGlobalLayoutListener
        keypadHeight = currentKeypadHeight
        if (keypadHeight > screenHeight * 0.15) {
            toast("Keyboard is showing")
        } else {
            toast("Keyboard is closed")
        }
    }
    contentView.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)

}