package template.ui

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.library.extension.hideKeyboard
import com.example.library.extension.post
import com.kotlin.app.R

abstract class BaseDialogFragment : DialogFragment(),
        FragmentView {

    /**
     * [DialogFragment] implements
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = object : Dialog(requireActivity(), dialogStyle()) {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutResource(), container, false)
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
        when (dialogStyle()) {
            R.style.App_Dialog_FullScreen,
            R.style.App_Dialog_FullScreen_Transparent,
            -> dialog?.window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        hideKeyboard()
        view.post(300) {
            super.onDismiss(dialog)
        }
    }

    /**
     * [FragmentView] implements
     */
    override val fragment: Fragment get() = this

    /**
     * [BaseDialog] properties
     */
    protected open fun dialogStyle(): Int {
        return R.style.App_Dialog
    }

    protected open fun onBackPressed() {
        dismissAllowingStateLoss()
    }

    protected open fun onWindowConfig(window: Window) = Unit

    fun Window.setFullScreen() {
        setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        attributes = attributes.also {
            it.gravity = Gravity.BOTTOM
            it.flags = it.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        }
    }

    fun Window.setGravityBottom() {
        attributes = attributes.also {
            it.gravity = Gravity.BOTTOM
            it.flags = it.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        }
    }

    fun Window.contentUnderStatusBar() {
        var isFloating = true
        val flagsToUpdate = (WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN and attributes.flags.inv())
        if (isFloating) {
            setFlags(0, flagsToUpdate)
        } else {
            setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, flagsToUpdate)
        }
    }
}