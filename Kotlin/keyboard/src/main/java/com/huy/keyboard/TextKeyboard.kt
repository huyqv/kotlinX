package com.huy.keyboard

import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.view_text_row.*

open class TextKeyboard : BaseKeyboard() {

    companion object {
        val instance: TextKeyboard  by lazy { TextKeyboard() }
    }

    private lateinit var charKeys: Array<TextView>

    private var shiftSilentCheck: Boolean = false

    override fun keyboardLayout() = R.layout.keyboard_text

    override fun deleteButton(): View = viewDel

    override fun enterButton(): TextView = viewDone

    override fun hideButton(): View = viewHide

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        charKeys = arrayOf(
                viewQ, viewW, viewE, viewR, viewT, viewY, viewU, viewI, viewO, viewP,
                viewA, viewS, viewD, viewF, viewG, viewH, viewJ, viewK, viewL,
                viewZ, viewX, viewC, viewV, viewB, viewN, viewM
        )

        addClickListeners(*charKeys, viewSpace)

        if (CustomKeyboard.currentOutput ?: false == true) {
            viewShift.isEnabled = false
            viewCaps.isEnabled = false
            upperCaseAllChar(true)
            return
        }

        viewShift.isEnabled = true
        viewCaps.isEnabled = true
        upperCaseAllChar(false)
        viewShift.setOnCheckedChangeListener { _, isChecked ->
            if (!shiftSilentCheck) {
                if (viewCaps.isChecked) upperCaseAllChar(!isChecked)
                else upperCaseAllChar(isChecked)
            }
        }
        viewCaps.setOnCheckedChangeListener { _, isChecked ->
            shiftSilentCheck = true
            viewShift.isChecked = false
            shiftSilentCheck = false
            upperCaseAllChar(isChecked)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            viewSpace -> commitText(" ")
            else -> {
                super.onClick(v)
                viewShift.isChecked = false
            }
        }
    }

    private fun upperCaseAllChar(isUpperCase: Boolean) {
        charKeys.forEach {
            if (isUpperCase) {
                it.text = it.text.toString().toUpperCase()
            } else {
                it.text = it.text.toString().toLowerCase()
            }
        }

    }

}