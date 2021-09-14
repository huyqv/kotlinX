package com.kotlin.app.ui.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.kotlin.app.R
import com.sample.library.extension.hideKeyboard

abstract class BaseDialogFragment<T : ViewBinding> : DialogFragment(),
    FragmentView {

    protected val bind: T by viewBinding(inflating())

    abstract fun inflating(): (LayoutInflater) -> ViewBinding

    /**
     * [DialogFragment] implements
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun getTheme(): Int {
        return R.style.App_Dialog_FullScreen_Transparent
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
                this@BaseDialogFragment.onBackPressed()
            }
        }
        dialog.window?.also {
            it.decorView.setBackgroundColor(Color.TRANSPARENT)
            it.attributes.windowAnimations = R.style.App_DialogAnim
            onWindowConfig(it)
        }
        dialog.setOnDismissListener {
            println("onDismiss")
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = bind.root
        view.setOnTouchListener { _, _ -> true }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
        onLiveDataObserve()
    }

    override fun onStart() {
        super.onStart()
        when (theme) {
            R.style.App_Dialog_FullScreen,
            R.style.App_Dialog_FullScreen_Transparent,
            -> dialog?.window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        hideKeyboard()
        post(200) {
            super.onDismiss(dialog)
        }
    }

    /**
     * [FragmentView] implements
     */
    override val fragment: Fragment get() = this

    override val backPressedCallback: OnBackPressedCallback = getBackPressCallBack()

    /**
     * [BaseDialogFragment] properties
     */
    protected open fun onWindowConfig(window: Window) = Unit

}