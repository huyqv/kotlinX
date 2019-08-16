package com.huy.keyboard

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.view_num_row.*

class QwertyKeyboard : TextKeyboard() {

    companion object {
        val instance: QwertyKeyboard  by lazy { QwertyKeyboard() }
    }

    override fun keyboardLayout() = R.layout.keyboard_qwerty

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addClickListeners(view1, view2, view3, view4, view5, view6, view7, view8, view9, view0)
    }

}