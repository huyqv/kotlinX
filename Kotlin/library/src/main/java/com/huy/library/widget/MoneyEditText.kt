package com.huy.library.widget

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import java.util.*

class MoneyEditText : AppCompatEditText, TextWatcher {

    constructor(context: Context) : super(context) {
        addTextChangedListener(this)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        addTextChangedListener(this)
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    override fun afterTextChanged(editable: Editable) {
        var inputText = text!!.toString().replace(".", "")
        if (inputText.length > 3) {
            val list = ArrayList<String>()
            while (inputText.length > 3) {


                val s = inputText.substring(inputText.length - 3, inputText.length)
                list.add(s)
                inputText = inputText.substring(0, inputText.length - 3)
            }
            if (!TextUtils.isEmpty(inputText))
                list.add(inputText)
            val fs = StringBuilder()
            for (i in list.indices.reversed()) {
                if (i == 0)
                    fs.append(list[i])
                else
                    fs.append(list[i]).append(".")
            }
            removeTextChangedListener(this)
            setText(fs.toString())
            setSelection(length())
            addTextChangedListener(this)
        }
    }

}
