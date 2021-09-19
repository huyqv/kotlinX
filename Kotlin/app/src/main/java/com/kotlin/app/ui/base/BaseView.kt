package com.kotlin.app.ui.base

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.sample.widget.extension.ViewClickListener

interface BaseView {

    val baseActivity: BaseActivity<*>? get() = null

    val lifecycleOwner: LifecycleOwner

    fun activityNavController(): NavController?

    fun onViewCreated()

    fun onLiveDataObserve()

    fun addClickListener(vararg views: View?) {
        val listener = object : ViewClickListener() {
            override fun onClicks(v: View?) {
                onViewClick(v)
            }
        }
        views.forEach { it?.setOnClickListener(listener) }
    }

    fun onViewClick(v: View?) = Unit

    fun add(fragment: Fragment, stack: Boolean = true)

    fun replace(fragment: Fragment, stack: Boolean = true)

    fun <T : Fragment> remove(cls: Class<T>)

    fun NavController?.navigate(
        @IdRes actionId: Int,
        block: (NavigationBuilder.() -> Unit)? = null
    ) {
        this ?: return
        NavigationBuilder(this).also {
            it.setAlphaAnim()
            block?.invoke(it)
            it.navigate(actionId)
        }
    }

    fun navigate(@IdRes actionId: Int, block: (NavigationBuilder.() -> Unit)? = null) {
        activityNavController().navigate(actionId, block)
    }

    val defaultArgKey: String get() = "DEFAULT_ARG_KEY"

    fun <T> navResultLiveData(key: String = defaultArgKey): MutableLiveData<T>? {
        return activityNavController()?.currentBackStackEntry?.savedStateHandle?.getLiveData(key)
    }

    fun <T> setNavResult(key: String?, result: T) {
        activityNavController()?.previousBackStackEntry?.savedStateHandle?.set(
            key
                ?: defaultArgKey, result
        )
    }

    fun <T> setNavResult(result: T) {
        activityNavController()?.previousBackStackEntry?.savedStateHandle?.set(
            defaultArgKey,
            result
        )
    }

    fun <T> LiveData<T>.observe(block: (T) -> Unit) {
        observe(lifecycleOwner, Observer(block))
    }

    fun <T> LiveData<T>.removeObservers() {
        removeObservers(lifecycleOwner)
    }

}