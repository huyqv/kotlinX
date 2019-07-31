package com.huy.library.view

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.huy.library.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/01
 * @Description:
 * In Fragment:
 * keyboardProvider = KeyboardProvider(activity!!)
 * view?.post { keyboardProvider!!.start() }
 * In Activity:
 * keyboardProvider = KeyboardProvider(this)
 * post { keyboardProvider!!.start() }
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class KeyboardProvider constructor(private val activity: Activity) : PopupWindow(activity) {

    interface Observer {
        fun onKeyboardHeightChanged(height: Int, orientation: Int)
    }

    private var observer: Observer? = null

    /** The cached landscape height of the keyboard  */
    private var keyboardLandscapeHeight: Int = 0

    /** The cached portrait height of the keyboard  */
    private var keyboardPortraitHeight: Int = 0

    private val popupView: View?

    private val parentView: View

    /**
     * Get the screen orientation
     *
     * @return the screen orientation
     */
    private val screenOrientation: Int
        get() = activity.resources.configuration.orientation

    init {

        val inflater = activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.popupView = inflater.inflate(R.layout.view_popup, null, false)
        contentView = popupView

        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED

        parentView = activity.findViewById<View>(android.R.id.content)

        width = 0
        height = WindowManager.LayoutParams.MATCH_PARENT

        popupView!!.viewTreeObserver.addOnGlobalLayoutListener {
            handleOnGlobalLayout()
        }
    }

    /**
     * Start the KeyboardProvider, this must be called after the onResume of the Activity.
     * PopupWindows are not allowed to be registered before the onResume has finished
     * of the Activity.
     */
    fun start() {
        if (!isShowing && parentView.windowToken != null) {
            setBackgroundDrawable(ColorDrawable(0))
            showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0)
        }
    }

    fun close() {
        this.observer = null
        dismiss()
    }

    fun onResume(observer: Observer) {
        this.observer = observer
    }

    fun onPause() {
        this.observer = null
    }

    fun setKeyboardHeightObserver(observer: Observer?) {
        this.observer = observer
    }

    /**
     * Popup window itself is as big as the window of the Activity.
     * The keyboard can then be calculated by extracting the popup view bottom
     * from the activity window height.
     */
    private fun handleOnGlobalLayout() {

        val screenSize = Point()
        activity.windowManager.defaultDisplay.getSize(screenSize)

        val rect = Rect()
        popupView!!.getWindowVisibleDisplayFrame(rect)

        val orientation = screenOrientation
        val keyboardHeight = screenSize.y - rect.bottom

        when {
            keyboardHeight == 0 -> notifyKeyboardHeightChanged(0, orientation)
            orientation == Configuration.ORIENTATION_PORTRAIT -> {
                this.keyboardPortraitHeight = keyboardHeight
                notifyKeyboardHeightChanged(keyboardPortraitHeight, orientation)
            }
            else -> {
                this.keyboardLandscapeHeight = keyboardHeight
                notifyKeyboardHeightChanged(keyboardLandscapeHeight, orientation)
            }
        }
    }

    private fun notifyKeyboardHeightChanged(height: Int, orientation: Int) {
        observer?.onKeyboardHeightChanged(height, orientation)
    }

}