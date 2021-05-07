package com.kotlin.app.ui.alert

import android.view.View
import android.widget.TextView
import com.example.library.extension.setHyperText
import com.example.library.extension.string
import com.kotlin.app.ui.base.BaseDialog
import com.kotlin.app.R
import kotlinx.android.synthetic.main.alert.*

class AlertFragment : BaseDialog() {

    private val alertVM by lazy { activityVM(AlertVM::class) }

    private val arg: AlertArg? get() = alertVM.arg.value

    private var hasBackClick: Boolean = false

    private var clickedView: View? = null

    override fun layoutResource(): Int {
        return R.layout.alert
    }

    override fun onViewCreated() {
    }

    override fun onLiveDataObserve() {
        alertVM.arg.observe {
            if (it != null) {
                onBindView(it)
            } else {
                dismissAllowingStateLoss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        arg?.also { onBindClick(it) }
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
        viewNeutral.onBindButton(arg.buttonNeutral)
        viewPositive.onBindButton(arg.buttonPositive)
        viewNegative.onBindButton(arg.buttonNegative)
    }

    private fun onBindClick(arg: AlertArg) {
        when {
            hasBackClick -> {
                arg.onDismissClick()
            }
            clickedView == viewNeutral -> {
                arg.onNeutralClick()
            }
            clickedView == viewPositive -> {
                arg.onPositiveClick()
            }
            clickedView == viewNegative -> {
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