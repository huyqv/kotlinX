package com.kotlin.app.ui.alert

import android.view.View
import android.widget.TextView
import com.kotlin.app.R
import com.example.library.extension.setHyperText
import com.example.library.extension.string
import kotlinx.android.synthetic.main.alert.*

class AlertView(val v: AlertFragment) {

    fun onBindArg(arg: AlertArg?) {
        arg ?: return
        onBindDialogSize(arg.headerGuideline)
        v.imageViewIcon.setImageResource(arg.icon)
        v.textViewTitle.text = arg.title ?: string(R.string.app_name)
        v.textViewMessage.setHyperText(arg.message)
        v.viewNeutral.onBindButton(arg.buttonNeutral, arg.onNeutral)
        v.viewPositive.onBindButton(arg.buttonPositive, arg.onPositive)
        v.viewNegative.onBindButton(arg.buttonNegative, arg.onNegative)
    }

    private fun TextView.onBindButton(text: String?, onClick: (AlertFragment) -> Unit) {
        if (text.isNullOrEmpty()) {
            this.visibility = View.GONE
        } else {
            this.text = text
            this.setOnClickListener {
                v.dismissAllowingStateLoss()
                onClick(v)
            }
        }
    }

    private fun onBindDialogSize(guidelineId: Int) {
        /* if (guidelineId == 0) return
         val viewId = v.confirmDialogContent.id
         ConstraintSet().apply {
             clone(v.viewContent)
             constrainHeight(viewId, ConstraintSet.MATCH_CONSTRAINT)
             constrainDefaultHeight(viewId, ConstraintSet.MATCH_CONSTRAINT)
             connect(viewId, ConstraintSet.TOP, guidelineId, ConstraintSet.TOP)
             applyTo(v.viewContent)
         }*/
    }

}