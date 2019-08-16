package com.huy.keyboard

import android.widget.EditText

interface KeyboardOutput {

    val outputId: Int

    var nextLabel: String?

    var nextAction: (() -> Unit)?

    var customInputType: Int

    var textAllCaps: Boolean

    val editText: EditText

}