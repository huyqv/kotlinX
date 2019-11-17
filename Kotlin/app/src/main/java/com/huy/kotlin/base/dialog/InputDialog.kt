package com.huy.kotlin.base.dialog

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.huy.kotlin.R
import kotlinx.android.synthetic.main.dialog_input.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/15
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class InputDialog(activity: FragmentActivity?) : BaseDialog(activity) {

    override fun layoutRes() = R.layout.dialog_input

    override fun View.onViewCreated() {
    }

    val input: String? get() = view?.editTextInput?.text.toString().replace("  ", " ").trim()

    fun label(stringRes: Int) {
        view?.textViewLabel?.setText(stringRes)
    }

    fun label(string: CharSequence) {
        view?.textViewLabel?.text = string
    }

    fun error(stringRes: Int) {
        view?.textViewError?.setText(stringRes)
    }

    fun error(string: CharSequence) {
        view?.textViewError?.text = string
    }

    fun hint(stringRes: Int) {
        view?.editTextInput?.setHint(stringRes)
    }

    fun hint(string: CharSequence) {
        view?.editTextInput?.hint = string
    }

    fun text(stringRes: Int) {
        view?.editTextInput?.setText(stringRes)
    }

    fun text(string: CharSequence) {
        view?.editTextInput?.setText(string)
    }

}