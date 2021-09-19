package com.kotlin.app.ui.base

import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

interface FragmentView : BaseView {

    val fragment: Fragment

    /**
     * [BaseView] implements
     */
    override val baseActivity: BaseActivity<*>? get() = fragment.requireActivity() as? BaseActivity<*>

    override val lifecycleOwner: LifecycleOwner get() = fragment.viewLifecycleOwner

    override fun activityNavController(): NavController? {
        return baseActivity?.activityNavController()
    }

    override fun add(fragment: Fragment, stack: Boolean) {
        baseActivity?.add(fragment, stack)
    }

    override fun replace(fragment: Fragment, stack: Boolean) {
        baseActivity?.replace(fragment, stack)
    }

    override fun <T : Fragment> remove(cls: Class<T>) {
        baseActivity?.remove(cls)
    }

    /**
     * [FragmentView] utils
     */
    fun <T : ViewBinding> viewBinding(block: (LayoutInflater) -> ViewBinding): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy { block.invoke(fragment.layoutInflater) as T }
    }

    fun requestFocus(v: View?) {
        launch(240) { v?.requestFocus() }
    }

    /**
     * Back press handle
     */
    val backPressedCallback: OnBackPressedCallback

    fun getBackPressCallBack(): OnBackPressedCallback {
        return object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
    }

    fun onBackPressed() {
        backPressedCallback.remove()
        fragment.requireActivity().onBackPressed()
    }

    /**
     * LifecycleScope
     */
    fun launch(delayInterval: Long, block: suspend CoroutineScope.() -> Unit) {
        fragment.lifecycleScope.launch {
            delay(delayInterval)
            block()
        }
    }

    fun launch(block: suspend CoroutineScope.() -> Unit) {
        fragment.lifecycleScope.launch {
            block()
        }
    }

    fun launch(delayInterval: Long, unit: Unit) {
        launch(delayInterval) { unit }
    }

}