package com.kotlin.app.ui.base

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.library.R
import com.example.library.extension.addFragment
import com.example.library.extension.removeFragment
import com.example.library.extension.replaceFragment

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseActivity : AppCompatActivity(), BaseView {

    /**
     * [AppCompatActivity] override
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource())
        onViewCreated()
        onLiveDataObserve()
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit)
    }

    /**
     * [BaseActivity] abstract implements
     */
    abstract fun layoutResource(): Int

    abstract fun onViewCreated()

    abstract fun onLiveDataObserve()

    /**
     * [BaseView] implement
     */
    final override val baseActivity: BaseActivity? get() = this

    final override fun add(fragment: Fragment, stack: Boolean) {
        addFragment(fragment, fragmentContainerId(), stack)
    }

    final override fun replace(fragment: Fragment, stack: Boolean) {
        replaceFragment(fragment, fragmentContainerId(), stack)
    }

    final override fun <T : Fragment> remove(cls: Class<T>) {
        removeFragment(cls)
    }

    /**
     * [BaseActivity] properties
     */
    val nav: NavController get() = findNavController(navigationHostId())

    protected open fun navigationHostId(): Int {
        throw NullPointerException("navigationHostId no has implement")
    }

    protected open fun fragmentContainerId(): Int {
        throw NullPointerException("fragmentContainerId no has implement")
    }

    fun <T> LiveData<T>.observe(block: (T) -> Unit) {
        observe(this@BaseActivity, Observer(block))
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        //return hideKeyboardOnTouchDown(event)
        return super.dispatchTouchEvent(event)
    }

    private fun hideKeyboardOnTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

}