package com.kotlin.app.ui.base

import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
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

    fun onBackPressed()

    /**
     * LifecycleScope
     */
    val lifecycleScope get() = fragment.lifecycleScope

    fun addObserver(observer: LifecycleObserver) {
        lifecycleOwner.lifecycle.addObserver(observer)
    }

    fun launch(delayInterval: Long, block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            delay(delayInterval)
            block()
        }
    }

    fun launch(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            block()
        }
    }

    fun launch(delayInterval: Long, unit: Unit) {
        launch(delayInterval) { unit }
    }

    /**
     * SupportFragmentManager transaction
     */
    fun add(fragment: Fragment, stack: Boolean) {
        baseActivity?.add(fragment, stack)
    }

    fun replace(fragment: Fragment, stack: Boolean) {
        baseActivity?.replace(fragment, stack)
    }

    fun <T : Fragment> remove(cls: Class<T>) {
        baseActivity?.remove(cls)
    }

    /**
     * Navigation
     */
    fun childNavigate(@IdRes actionId: Int, block: (NavigationBuilder.() -> Unit)? = null) {
        fragment.findNavController().navigate(actionId, block)
    }

    fun childPopBackStack(@IdRes fragmentId: Int = 0, inclusive: Boolean = false) {
        if (fragmentId != 0) {
            fragment.findNavController().popBackStack(fragmentId, inclusive)
        } else {
            fragment.findNavController().popBackStack()
        }
    }

    fun mainNavigate(@IdRes actionId: Int, block: (NavigationBuilder.() -> Unit)? = null) {
        activityNavController()?.navigate(actionId, block)
    }

    fun mainPopBackStack(@IdRes fragmentId: Int = 0, inclusive: Boolean = false) {
        if (fragmentId != 0) {
            activityNavController()?.popBackStack(fragmentId, inclusive)
        } else {
            activityNavController()?.popBackStack()
        }
    }

}