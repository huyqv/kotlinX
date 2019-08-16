package com.huy.keyboard

import android.text.InputType
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction


object CustomKeyboard {

    private val hiddenFragment = Fragment()

    @Volatile
    var isShown = false

    @Volatile
    var isConnected = false

    @Volatile
    var isProcess = false

    private var currentKeyboard: BaseKeyboard? = null

    var currentOutput: KeyboardOutput? = null

    fun showKeyboard(ko: KeyboardOutput) {
        if (isProcess) return
        isProcess = true
        val ioHasChange = ioUpdate(ko)
        val keyboard = getKeyboard(ko)
        if (ioHasChange) isConnected = false
        if (keyboard != currentKeyboard && currentKeyboard != null) hideKeyboard()
        currentKeyboard?.outputId = 0
        currentOutput = ko
        if (!isShown || keyboard != currentKeyboard || currentKeyboard == null) {
            currentKeyboard = getKeyboard(ko)
            showKeyboardFragment()
        }
        if (!isConnected) connectKeyboard()
        isProcess = false
    }

    fun hideKeyboard() {
        if (isShown) hideKeyboardFragment()
    }

    /**
     * show keyboard via bottom sheet dialog fragment
     */
    private fun showKeyboardDialog() {
        val output = currentOutput ?: return
        val keyboard = currentKeyboard ?: return
        val activity = output.editText.context as? FragmentActivity ?: return
        keyboard.show(activity.supportFragmentManager, keyboard::javaClass.name)
        isShown = true
        isConnected = true
    }

    /**
     * hide keyboard via bottom sheet dialog fragment
     */
    private fun hideKeyboardDialog() {
        currentKeyboard?.dismiss()
        isShown = false
    }

    /**
     * show keyboard via fragment to keyboard container of keyboard owner
     */
    private fun showKeyboardFragment() {
        val keyboard = currentKeyboard ?: return
        val context = currentOutput?.editText?.context ?: return
        val container = (context as? KeyboardOwner)?.keyboardContainer() ?: return
        transaction(context as? FragmentActivity) {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            replace(container.id, keyboard, keyboard.javaClass.simpleName)
            isShown = true
        }
    }

    /**
     * hide keyboard via bottom sheet dialog fragment
     */
    private fun hideKeyboardFragment() {
        val keyboard = currentKeyboard ?: return
        transaction(currentOutput?.editText?.context as? FragmentActivity) {
            remove(keyboard)
            isShown = false
        }
    }


    private fun transaction(activity: FragmentActivity?, block: FragmentTransaction.() -> Unit) {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            setCustomAnimations(R.anim.anim_keyboard_show, R.anim.anim_keyboard_hide,
                    R.anim.anim_keyboard_show, R.anim.anim_keyboard_hide)
            block()
            commitAllowingStateLoss()
        }
    }

    /**
     * return none null editText if connected
     */
    private fun connectKeyboard() {
        val output = currentOutput ?: return
        val keyboard = currentKeyboard ?: return
        val editText = currentOutput?.editText ?: return
        editText.setRawInputType(output.customInputType)
        editText.setTextIsSelectable(false)
        editText.setSelection(editText.text.toString().length)
        keyboard.inputConnection = editText.onCreateInputConnection(EditorInfo())
        keyboard.outputId = output.outputId
        isConnected = true
    }

    /**
     * return keyboard by keyboard output type
     */
    private fun getKeyboard(ko: KeyboardOutput): BaseKeyboard? {
        return when (ko.customInputType) {
            97 -> TextKeyboard.instance
            InputType.TYPE_CLASS_NUMBER -> NumberKeyboard.instance
            InputType.TYPE_NULL -> null
            else -> QwertyKeyboard.instance
        }
    }

    private fun ioUpdate(ko: KeyboardOutput): Boolean {
        if (ko.outputId == currentOutput?.outputId) return false
        return true
    }

}