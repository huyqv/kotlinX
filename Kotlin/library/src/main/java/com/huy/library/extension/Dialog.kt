package com.huy.library.extension

import android.app.DatePickerDialog
import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.huy.library.R
import java.util.*


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/30
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
private fun Context.getText(@StringRes title: Int?): String? {
    return if (null == title) null else getString(title)
}

private fun Context.initDialog(title: String?, msg: String, block: (AlertDialog.Builder.() -> Unit)? = null) {
    val dialog = AlertDialog.Builder(this).setMessage(msg)
    if (null != title) {
        dialog.setTitle(title)
    } else {
        dialog.setTitle(getString(R.string.app_name))
    }
    if (null != block) {
        dialog.block()
    }
    dialog.create().show()
}

@Suppress("DEPRECATION")
fun Context.getProgressDialog(msg: String = "Please wait ..."): android.app.ProgressDialog {
    val dialog = android.app.ProgressDialog(this)
    dialog.setMessage(msg)
    return dialog
}

@Suppress("DEPRECATION")
fun Context.getProgressDialog(@StringRes msg: Int): android.app.ProgressDialog {
    return getProgressDialog(getString(msg))
}

fun Context.showMessageDialog(title: String?, msg: String) {
    initDialog(title, msg) {
        setPositiveButton("CLOSE", null)
    }
}

fun Context.showMessageDialog(@StringRes title: Int?, @StringRes msg: Int) {
    showMessageDialog(getText(title), getString(msg))
}

fun Context.showConfirmDialog(title: String?, msg: String, block: () -> Unit) {
    initDialog(title, msg) {
        setNegativeButton("CLOSE", null)
        setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            block()
        }
    }
}

fun Context.showConfirmDialog(@StringRes title: Int?, @StringRes msg: Int, block: () -> Unit) {
    showConfirmDialog(getText(title), getString(msg), block)
}

fun Context.showDialog(title: String?, msg: String, positiveBlock: () -> Unit, negativeBlock: () -> Unit) {
    initDialog(title, msg) {
        setNegativeButton("NO") { dialog, _ ->
            dialog.dismiss()
            negativeBlock()
        }
        setPositiveButton(R.string.yes) { dialog, _ ->
            dialog.dismiss()
            positiveBlock()
        }
        setNeutralButton("CANCEL", null)
    }
}

fun Context?.showDateDialog(minDate: Long, maxDate: Long = System.currentTimeMillis(), block: (Int, Int, Int) -> Unit) {

    this ?: return
    val cal = Calendar.getInstance()
    val y = cal.get(Calendar.YEAR)
    val m = cal.get(Calendar.MONTH)
    val d = cal.get(Calendar.DAY_OF_MONTH)

    val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        val correctMonth = month + 1
        block(year, correctMonth, dayOfMonth)
    }, y, m, d)
    dialog.datePicker.maxDate = maxDate
    dialog.datePicker.minDate = minDate
    dialog.show()

}

fun Context?.showDateDialog(minDate: Long, maxDate: Long = System.currentTimeMillis(), block: (String) -> Unit) {
    @Suppress("IMPLICIT_CAST_TO_ANY")
    showDateDialog(minDate, maxDate) { y, m, d ->
        block("${if (d < 10) "0$d" else d}-${if (m < 10) "0$m" else m}-$y")
    }
}

fun Fragment.showDateDialog(minDate: Long, maxDate: Long = System.currentTimeMillis(), block: (Int, Int, Int) -> Unit) {
    context.showDateDialog(minDate, maxDate, block)
}

fun Fragment.showDateDialog(minDate: Long, maxDate: Long = System.currentTimeMillis(), block: (String) -> Unit) {
    @Suppress("IMPLICIT_CAST_TO_ANY")
    showDateDialog(minDate, maxDate) { y, m, d ->
        block("${if (d < 10) "0$d" else d}-${if (m < 10) "0$m" else m}-$y")
    }
}
