package com.kotlin.app.ui.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.sample.library.R

class NavigationBuilder(private val navController: NavController) {

    val options = NavOptions.Builder()

    var args: Bundle? = null

    var extras: Navigator.Extras? = null

    fun clearBackStack() {
        options.setLaunchSingleTop(true)
        navController.graph.id.also {
            options.setPopUpTo(it, true)
        }
    }

    fun setPopUpTo(@IdRes fragmentId: Int) {
        options.setLaunchSingleTop(true)
        options.setPopUpTo(fragmentId, false)
    }

    fun setParallaxAnim(reserved: Boolean = false) {

        if (reserved) options.apply {
            setEnterAnim(R.anim.parallax_pop_enter)
            setExitAnim(R.anim.parallax_pop_exit)
            setPopEnterAnim(R.anim.parallax_enter)
            setPopExitAnim(R.anim.parallax_exit)
        } else options.apply {
            setEnterAnim(R.anim.parallax_enter)
            setExitAnim(R.anim.parallax_exit)
            setPopEnterAnim(R.anim.parallax_pop_enter)
            setPopExitAnim(R.anim.parallax_pop_exit)
        }


    }

    fun setHorizontalAnim(reserved: Boolean = false) {
        if (reserved) options.apply {
            setEnterAnim(R.anim.horizontal_pop_enter)
            setExitAnim(R.anim.horizontal_pop_exit)
            setPopEnterAnim(R.anim.horizontal_enter)
            setPopExitAnim(R.anim.horizontal_exit)
        } else options.apply {
            setEnterAnim(R.anim.horizontal_enter)
            setExitAnim(R.anim.horizontal_exit)
            setPopEnterAnim(R.anim.horizontal_pop_enter)
            setPopExitAnim(R.anim.horizontal_pop_exit)
        }

    }

    fun setVerticalAnim(reserved: Boolean = false) {
        if (reserved) options.apply {
            setEnterAnim(R.anim.vertical_pop_enter)
            setExitAnim(R.anim.vertical_pop_exit)
            setPopEnterAnim(R.anim.vertical_enter)
            setPopExitAnim(R.anim.vertical_exit)
        } else options.apply {
            setEnterAnim(R.anim.vertical_enter)
            setExitAnim(R.anim.vertical_exit)
            setPopEnterAnim(R.anim.vertical_pop_enter)
            setPopExitAnim(R.anim.vertical_pop_exit)
        }
    }

    fun setAlphaAnim() {
        options.apply {
            setEnterAnim(R.anim.alpha01)
            setExitAnim(R.anim.alpha10)
            setPopEnterAnim(R.anim.alpha01)
            setPopExitAnim(R.anim.alpha10)
        }
    }

    fun navigate(@IdRes actionId: Int) {
        navController.navigate(actionId, args, options.build(), extras)
    }
}