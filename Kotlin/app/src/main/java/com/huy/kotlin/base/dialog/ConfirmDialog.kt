package com.huy.kotlin.base.dialog

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import com.huy.kotlin.R
import kotlinx.android.synthetic.main.dialog_confirm.view.*
import kotlinx.android.synthetic.main.dialog_title.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/18
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ConfirmDialog(activity: FragmentActivity?) : BaseDialog(activity) {

    override fun layoutRes() = R.layout.dialog_confirm

    override fun View.onViewCreated() {
        viewClose.setOnClickListener { dismiss() }
    }

    fun drawable(@DrawableRes drawableRes: Int?) {
        drawableRes ?: return
        view?.imageViewIcon?.setImageResource(drawableRes)
    }

    fun title(@StringRes res: Int) {
        view?.textViewTitle?.setText(res)
    }

    fun title(title: String) {
        view?.textViewTitle?.text = title
    }

    fun message(@StringRes message: Int) {
        view?.textViewMessage?.setText(message)
    }

    fun message(message: String) {
        view?.textViewMessage?.text = message
    }

    fun onConfirm(block: () -> Unit) {
        view?.viewConfirm?.setOnClickListener {
            block()
            dismiss()
        }
    }

}