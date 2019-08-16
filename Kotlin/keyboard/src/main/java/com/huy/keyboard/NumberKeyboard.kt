package com.huy.keyboard

import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.keyboard_number.*

class NumberKeyboard : BaseKeyboard() {

    companion object {
        val instance: NumberKeyboard  by lazy { NumberKeyboard() }
    }

    override fun keyboardLayout() = R.layout.keyboard_number

    override fun deleteButton(): View = viewDel

    override fun enterButton(): TextView = viewDone

    override fun hideButton(): View = viewHide

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addClickListeners(view1, view2, view3, view4, view5, view6, view7, view8, view9, view0)
    }

}