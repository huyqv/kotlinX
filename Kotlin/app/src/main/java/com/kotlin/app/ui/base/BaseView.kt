package com.kotlin.app.ui.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.example.library.extension.ViewClickListener
import com.kotlin.app.R
import kotlin.reflect.KClass

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface BaseView {

    val baseActivity: BaseActivity?

    val lifecycleOwner: LifecycleOwner

    val navController: NavController?

    fun layoutResource(): Int

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

    fun <T : ViewModel> ViewModelStoreOwner.viewModel(cls: KClass<T>): T {
        return ViewModelProvider(this).get(cls.java)
    }

    fun <T : ViewModel> ViewModelStoreOwner.newVM(cls: KClass<T>): T {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[cls.java]
    }

    fun <T : ViewModel> Fragment.activityVM(cls: KClass<T>): T {
        return ViewModelProvider(requireActivity()).get(cls.java)
    }

    fun <T : ViewModel> AppCompatActivity.activityVM(cls: KClass<T>): T {
        return ViewModelProvider(this).get(cls.java)
    }

    fun navigate(directions: NavDirections, extras: Navigator.Extras? = null, block: (NavOptions.Builder.() -> Unit) = {}) {
        val options = NavOptions.Builder().also {
            it.setVerticalAnim()
            it.block()
        }.build()
        navController?.navigate(directions.actionId, null, options, extras)

    }

    fun navigateUp() {
        navController?.navigateUp()
    }

    fun NavOptions.Builder.setParallaxAnim(reserved: Boolean = false) {
        if (reserved) {
            setEnterAnim(R.anim.parallax_pop_enter)
            setExitAnim(R.anim.parallax_pop_exit)
            setPopEnterAnim(R.anim.parallax_enter)
            setPopExitAnim(R.anim.parallax_exit)
        } else {
            setEnterAnim(R.anim.parallax_enter)
            setExitAnim(R.anim.parallax_exit)
            setPopEnterAnim(R.anim.parallax_pop_enter)
            setPopExitAnim(R.anim.parallax_pop_exit)
        }
    }

    fun NavOptions.Builder.setHorizontalAnim(reserved: Boolean = false) {
        if (reserved) {
            setEnterAnim(R.anim.horizontal_pop_enter)
            setExitAnim(R.anim.horizontal_pop_exit)
            setPopEnterAnim(R.anim.horizontal_enter)
            setPopExitAnim(R.anim.horizontal_exit)
        } else {
            setEnterAnim(R.anim.horizontal_enter)
            setExitAnim(R.anim.horizontal_exit)
            setPopEnterAnim(R.anim.horizontal_pop_enter)
            setPopExitAnim(R.anim.horizontal_pop_exit)
        }

    }

    fun NavOptions.Builder.setVerticalAnim(): NavOptions.Builder {
        setEnterAnim(R.anim.vertical_enter)
        setExitAnim(R.anim.vertical_exit)
        setPopEnterAnim(R.anim.vertical_pop_enter)
        setPopExitAnim(R.anim.vertical_pop_exit)
        return this
    }

    fun NavOptions.Builder.setLaunchSingleTop(): NavOptions.Builder {
        setLaunchSingleTop(true)
        navController?.graph?.id?.also {
            setPopUpTo(it, false)
        }
        return this
    }

    fun <T> LiveData<T>.observe(block: (T) -> Unit) {
        observe(lifecycleOwner, Observer(block))
    }

    fun <T> LiveData<T>.removeObservers() {
        removeObservers(lifecycleOwner)
    }

    fun add(fragment: Fragment, stack: Boolean = true) {
        baseActivity?.add(fragment, stack)
    }

    fun replace(fragment: Fragment, stack: Boolean = true) {
        baseActivity?.replace(fragment, stack)
    }

    fun <T : Fragment> remove(cls: Class<T>) {
        baseActivity?.remove(cls)
    }

}