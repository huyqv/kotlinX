package com.huy.kotlin.base.dialog

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import com.huy.kotlin.R
import kotlinx.android.synthetic.main.dialog_message.view.*
import kotlinx.android.synthetic.main.dialog_title.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/18
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class MessageDialog(activity: FragmentActivity?) : BaseDialog(activity) {

    override fun layoutRes() = R.layout.dialog_message

    override fun View.onViewCreated() {
        view?.viewClose?.setOnClickListener { dismiss() }
    }

    fun drawable(@DrawableRes drawable: Int?) {
        drawable ?: return
        view?.imageViewIcon?.setImageResource(drawable)
    }

    fun title(@StringRes res: Int) {
        view?.dialogTextViewTitle?.setText(res)
    }

    fun title(title: String) {
        view?.dialogTextViewTitle?.text = title
    }

    fun message(@StringRes message: Int) {
        view?.textViewMessage?.setText(message)
    }

    fun message(message: String) {
        view?.textViewMessage?.text = message
    }

}