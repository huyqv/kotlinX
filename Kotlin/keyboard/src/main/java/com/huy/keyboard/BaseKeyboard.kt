package com.huy.keyboard

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputConnection
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseKeyboard : BottomSheetDialogFragment(), View.OnClickListener, View.OnTouchListener {

    var inputConnection: InputConnection? = null

    var outputId: Int = 0

    private val pressHandler: Handler = Handler()

    private lateinit var deleteRunnable: Runnable

    private var colorTintText: Int = 0

    private var colorTintWhite: Int = 0

    private var hadConfig: Boolean = false

    @LayoutRes
    protected abstract fun keyboardLayout(): Int

    protected abstract fun deleteButton(): View

    protected abstract fun enterButton(): TextView

    protected abstract fun hideButton(): View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Key_Fragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(keyboardLayout(), container, false)
        colorTintText = ContextCompat.getColor(context!!, R.color.colorKeyText)
        colorTintWhite = ContextCompat.getColor(context!!, android.R.color.white)
        /* val lp = container?.layoutParams ?: return view
         lp.height = view!!.height
         container.layoutParams = lp*/
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteRunnable = Runnable {
            onDeleteChar()
            pressHandler.postDelayed(deleteRunnable, 50)
        }
        deleteButton().setOnTouchListener(this)
        addClickListeners(enterButton(), hideButton())
        CustomKeyboard.isShown = true
        configDialog()
        updateNextLabel()
    }

    override fun onDestroyView() {
        CustomKeyboard.isShown = false
        super.onDestroyView()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        val imageView = (v as? ImageView) ?: return false
        if (event?.action == MotionEvent.ACTION_DOWN) {
            onDeleteChar()
            pressHandler.postDelayed(deleteRunnable, 1000)
            imageView.post {
                imageView.setBackgroundResource(R.drawable.bg_key_blue)
                imageView.setColorFilter(colorTintWhite)
            }
            return true
        }

        if (event?.action == MotionEvent.ACTION_UP) {
            pressHandler.removeCallbacks(deleteRunnable)
            (v as? ImageView)?.apply {
                post {
                    setBackgroundResource(R.drawable.bg_key_white)
                    imageView.setColorFilter(colorTintText)
                }
            }
        }
        return false
    }

    fun addClickListeners(vararg views: View) {
        for (v in views) v.setOnClickListener(this)
    }

    fun commitText(s: String) {
        inputConnection?.commitText(s, 1)
    }

    override fun onClick(v: View?) {
        when (v) {
            enterButton() -> onNextButtonClick()
            deleteButton() -> onDeleteChar()
            hideButton() -> CustomKeyboard.hideKeyboard()
            is TextView -> commitText(v.text.toString())
        }
    }

    private fun onDeleteChar() {
        inputConnection?.apply {
            val selectedText = getSelectedText(0)
            if (selectedText.isNullOrEmpty()) deleteSurroundingText(1, 0)
            else commitText("", 1)
        }
    }

    private fun onNextButtonClick() {
        CustomKeyboard.currentOutput?.apply {
            if (nextAction != null) {
                nextAction!!()
                CustomKeyboard.hideKeyboard()
                return@apply
            }
            val nextViewId = (this as? View)?.nextFocusForwardId ?: return@apply
            val view = (context as? Activity)?.findViewById<View>(nextViewId)
            val keyboardOutput = view as? KeyboardOutput ?: return@apply
            keyboardOutput.editText.requestFocus()
        }
    }

    private fun configDialog() {
        if (hadConfig) return
        hadConfig = true
        val bottomDialog = dialog as? BottomSheetDialog ?: return
        val bottomSheet = bottomDialog.findViewById<View>(R.id.design_bottom_sheet)
        val coordinatorLayout = bottomSheet?.parent as? CoordinatorLayout
        if (coordinatorLayout == null) {
            CustomKeyboard.isShown = false
            dismiss()
            return
        }
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.peekHeight = bottomSheet.height
        coordinatorLayout.parent.requestLayout()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        updateNextLabel()
        hideSystemUI()
    }

    private fun updateNextLabel() {
        enterButton().text = CustomKeyboard.currentOutput?.nextLabel
                ?: "Next"
    }

    private fun hideSystemUI() {
        val view = activity?.window?.decorView
        view?.post {
            view.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

}