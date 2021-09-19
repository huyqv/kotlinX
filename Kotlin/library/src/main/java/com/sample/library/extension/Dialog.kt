package com.sample.library.extension

import android.app.DatePickerDialog
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import java.util.*

private fun LifecycleOwner.initDialog(
    title: String?,
    msg: String,
    block: (AlertDialog.Builder.() -> Unit)? = null
) {
    val activity = requireActivity() ?: return
    val dialog = AlertDialog.Builder(activity).setMessage(msg)
    if (null != title) {
        dialog.setTitle(title)
    } else {
        dialog.setTitle("")
    }
    if (null != block) {
        dialog.block()
    }
    dialog.create().show()
}

fun LifecycleOwner.showMessageDialog(title: String?, msg: String) {
    initDialog(title, msg) {
        setPositiveButton("CLOSE", null)
    }
}

fun LifecycleOwner.showConfirmDialog(title: String?, msg: String, block: () -> Unit) {
    initDialog(title, msg) {
        setNegativeButton("CLOSE") { dialog, _ ->
            dialog.dismiss()
        }
        setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            block()
        }
    }
}

fun LifecycleOwner.showDialog(
    title: String?,
    msg: String,
    positiveBlock: () -> Unit,
    negativeBlock: () -> Unit
) {
    initDialog(title, msg) {
        setNegativeButton("NO") { dialog, _ ->
            dialog.dismiss()
            negativeBlock()
        }
        setPositiveButton("YES") { dialog, _ ->
            dialog.dismiss()
            positiveBlock()
        }
        setNeutralButton("CANCEL", null)
    }
}

fun LifecycleOwner.showDateDialog(
    minDate: Long,
    maxDate: Long = System.currentTimeMillis(),
    block: (Int, Int, Int) -> Unit
) {

    val activity = requireActivity() ?: return
    val cal = Calendar.getInstance()
    val y = cal.get(Calendar.YEAR)
    val m = cal.get(Calendar.MONTH)
    val d = cal.get(Calendar.DAY_OF_MONTH)

    val dialog = DatePickerDialog(activity, { _, year, month, dayOfMonth ->
        val correctMonth = month + 1
        block(year, correctMonth, dayOfMonth)
    }, y, m, d)
    dialog.datePicker.maxDate = maxDate
    dialog.datePicker.minDate = minDate
    dialog.show()
}

fun LifecycleOwner.dateTextPicker(
    minDate: Long,
    maxDate: Long = System.currentTimeMillis(),
    block: (String) -> Unit
) {
    @Suppress("IMPLICIT_CAST_TO_ANY")
    showDateDialog(minDate, maxDate) { y, m, d ->
        block("${if (d < 10) "0$d" else d}-${if (m < 10) "0$m" else m}-$y")
    }
}

fun LifecycleOwner.timestampPicker(
    minDate: Long,
    maxDate: Long = System.currentTimeMillis(),
    block: (Long) -> Unit
) {
    @Suppress("IMPLICIT_CAST_TO_ANY")
    showDateDialog(minDate, maxDate) { y, m, d ->
        val cal = Calendar.getInstance()
        cal.set(y, m, d)
        block(cal.timeInMillis)
    }
}



