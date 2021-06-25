package sample.ui.alert

import android.view.View
import android.widget.TextView
import com.example.library.extension.setHyperText
import com.example.library.extension.string
import com.kotlin.app.R
import com.kotlin.app.databinding.AlertBinding
import sample.ui.main.MainDialogFragment

class AlertFragment : MainDialogFragment<AlertBinding>(AlertBinding::inflate) {

    private var hasBackClick: Boolean = false

    private var clickedView: View? = null

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
        bind.imageViewIcon.setImageResource(arg.icon)
        bind.textViewTitle.text = arg.title ?: string(R.string.app_name)
        bind.textViewMessage.setHyperText(arg.message)
        bind.viewNeutral.onBindButton(arg.buttonNeutral)
        bind.viewPositive.onBindButton(arg.buttonPositive)
        bind.viewNegative.onBindButton(arg.buttonNegative)
    }

    private fun onBindClick(arg: AlertArg) {
        arg.onDismiss()
        when {
            hasBackClick -> {
                arg.onDismissClick()
            }
            clickedView == bind.viewNeutral -> {
                arg.onNeutralClick()
            }
            clickedView == bind.viewPositive -> {
                arg.onPositiveClick()
            }
            clickedView == bind.viewNegative -> {
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