package com.sample.widget.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout

class AppRadioGroup : ConstraintLayout {

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    var onCheckedChanged: ((RadioButton, Boolean) -> Unit)? = null

    private val checkedChangeListener : CompoundButton.OnCheckedChangeListener by lazy {
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(onCheckedChanged != null && buttonView is RadioButton) {
                onCheckedChanged!!(buttonView, isChecked)
            }
        }
    }

    override fun addViewInLayout(child: View?, index: Int, params: ViewGroup.LayoutParams?): Boolean {
        (child as? RadioButton)?.also { radioButton ->
            radioButton.setOnCheckedChangeListener(checkedChangeListener)
        }
        return super.addViewInLayout(child, index, params)
    }

}