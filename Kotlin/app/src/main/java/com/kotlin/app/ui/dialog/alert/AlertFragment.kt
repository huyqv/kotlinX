package com.kotlin.app.ui.dialog.alert

import android.view.View
import android.widget.TextView
import com.example.library.extension.setHyperText
import com.example.library.extension.string
import com.kotlin.app.R
import com.kotlin.app.ui.main.MainDialogFragment
import kotlinx.android.synthetic.main.alert.*

class AlertFragment : MainDialogFragment() {

    private var hasBackClick: Boolean = false

    private var clickedView: View? = null

    override fun layoutResource(): Int {
        return R.layout.alert
    }

    override fun onViewCreated() {
    }

    override fun onLiveDataObserve() {
        dialogVM.alertLiveData.observe {
            if (it != null) {
                onBindView(it)
            } else {
                dismissAllowingStateLoss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialogVM.alertLiveData.value?.also { onBindClick(it) }
    }

    override fun onBackPressed() {
        hasBackClick = true
        super.onBackPressed()
    }

    /**
     *
     */
    private fun onBindView(arg: AlertArg) {
        imageViewIcon.setImageResource(arg.icon)
        textViewTitle.text = arg.title ?: string(R.string.app_name)
        textViewMessage.setHyperText(arg.message)
        dialogViewNeutral.onBindButton(arg.buttonNeutral)
        dialogViewPositive.onBindButton(arg.buttonPositive)
        dialogViewNegative.onBindButton(arg.buttonNegative)
    }

    private fun onBindClick(arg: AlertArg) {
        arg.onDismiss()
        when {
            hasBackClick -> {
                arg.onDismissClick()
            }
            clickedView == dialogViewNeutral -> {
                arg.onNeutralClick()
            }
            clickedView == dialogViewPositive -> {
                arg.onPositiveClick()
            }
            clickedView == dialogViewNegative -> {
                arg.onNegativeClick()
            }
        }
    }

    private fun TextView.onBindButton(text: String?) {
        if (text.isNullOrEmpty()) {
            this.visibility = View.GONE
        } else {
            this.text = text
            this.setOnClickListener {
                clickedView = this
                dismissAllowingStateLoss()
            }
        }
    }

}