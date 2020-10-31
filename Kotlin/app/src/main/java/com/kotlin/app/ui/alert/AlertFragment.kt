package com.kotlin.app.ui.alert

import com.kotlin.app.R
import com.example.library.ui.BaseDialog

class AlertFragment : BaseDialog() {

    private val alertVM by lazy { activityVM(AlertVM::class) }

    private val alertView by lazy { AlertView(this) }

    override fun layoutResource(): Int {
        return R.layout.alert
    }

    override fun onViewCreated() {
    }

    override fun onLiveDataObserve() {
        alertVM.arg.observe {
            if (it != null) {
                alertView.onBindArg(it)
            } else {
                dismiss()
            }
        }
    }


}