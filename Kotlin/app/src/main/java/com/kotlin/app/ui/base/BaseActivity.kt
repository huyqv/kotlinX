package com.kotlin.app.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.sample.library.R
import com.sample.library.extension.addFragment
import com.sample.library.extension.removeFragment
import com.sample.library.extension.replaceFragment
import com.sample.library.util.Logger

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity(),
    BaseView {

    protected val log: Logger by lazy { Logger(this::class) }

    val bind: T by viewBinding(inflating())

    abstract fun inflating(): (LayoutInflater) -> ViewBinding

    protected fun <T : ViewBinding> viewBinding(block: (LayoutInflater) -> ViewBinding): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy { block.invoke(layoutInflater) as T }
    }

    /**
     * [AppCompatActivity] implements
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        onViewCreated()
        onLiveDataObserve()
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        super.startActivity(intent, options)
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        /*if (event.action == MotionEvent.ACTION_DOWN) {
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
        }*/
        return super.dispatchTouchEvent(event)
    }

    /**
     * [BaseView] implements
     */
    final override val baseActivity: BaseActivity<*>? get() = this

    final override val lifecycleOwner: LifecycleOwner get() = this

    /**
     * SupportFragmentManager transaction
     */
    protected open fun fragmentContainerId(): Int {
        throw NullPointerException("fragmentContainerId no has implement")
    }

    fun add(fragment: Fragment, stack: Boolean) {
        addFragment(fragment, fragmentContainerId(), stack)
    }

    fun replace(fragment: Fragment, stack: Boolean) {
        replaceFragment(fragment, fragmentContainerId(), stack)
    }

    fun <T : Fragment> remove(cls: Class<T>) {
        removeFragment(cls)
    }

}