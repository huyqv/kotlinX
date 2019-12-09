package com.huy.library.extension

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.huy.library.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/02/24
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */

val HORIZONTAL_ANIMATIONS = intArrayOf(R.anim.horizontal_enter, R.anim.horizontal_exit, R.anim.horizontal_pop_enter, R.anim.horizontal_pop_exit)

val VERTICAL_ANIMATIONS = intArrayOf(R.anim.horizontal_enter, R.anim.horizontal_exit, R.anim.horizontal_pop_enter, R.anim.horizontal_pop_exit)

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun Fragment.addFragment(fragment: Fragment, @IdRes container: Int, backStack: Boolean = true, animations: IntArray? = VERTICAL_ANIMATIONS) {

    val tag = fragment::class.java.simpleName.tag()
    childFragmentManager.scheduleTransaction({
        add(container, fragment, tag)
        if (backStack) addToBackStack(tag)
    }, animations)
}

fun Fragment.replaceFragment(fragment: Fragment, @IdRes container: Int, backStack: Boolean = true, animations: IntArray? = VERTICAL_ANIMATIONS) {

    val tag = fragment::class.java.simpleName.tag()
    childFragmentManager.scheduleTransaction({
        replace(container, fragment, tag)
        if (backStack) addToBackStack(tag)
    }, animations)
}

fun Fragment.remove(cls: Class<Fragment>, animations: IntArray? = VERTICAL_ANIMATIONS) {

    val tag = cls.simpleName.tag()
    val fragment = childFragmentManager.findFragmentByTag(tag) ?: return
    childFragmentManager.scheduleTransaction({
        remove(fragment)
    }, animations)
}

fun Fragment.statusBarDrawable(@DrawableRes res: Int) {
    activity?.statusBarDrawable(res)
}

fun Fragment.statusBarDrawable(drawable: Drawable?) {
    activity?.statusBarDrawable(drawable)
}

fun FragmentActivity.addFragment(fragment: Fragment, @IdRes container: Int, backStack: Boolean = true, animations: IntArray? = VERTICAL_ANIMATIONS) {

    val tag = fragment::class.java.simpleName.tag()
    supportFragmentManager.scheduleTransaction({
        add(container, fragment, tag)
        if (backStack) addToBackStack(tag)
    }, animations)
}

fun FragmentActivity.replaceFragment(fragment: Fragment, @IdRes container: Int, backStack: Boolean = true, animations: IntArray? = VERTICAL_ANIMATIONS) {

    val tag = fragment::class.java.simpleName.tag()
    supportFragmentManager.scheduleTransaction({
        replace(container, fragment, tag)
        if (backStack) addToBackStack(tag)
    }, animations)
}

fun FragmentActivity.remove(cls: Class<*>, animations: IntArray? = VERTICAL_ANIMATIONS) {

    val tag = cls.simpleName.tag()
    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: return
    supportFragmentManager.scheduleTransaction({
        remove(fragment)
    }, animations)
}

fun FragmentActivity.clearStack() {
    val sfm = this.supportFragmentManager
    for (fragment in sfm.fragments) {
        if (fragment !is Fragment) continue
        sfm.beginTransaction().remove(fragment).commit()
    }
    sfm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun FragmentManager.scheduleTransaction(block: FragmentTransaction.() -> Unit, animations: IntArray? = VERTICAL_ANIMATIONS) {

    val transaction = beginTransaction()
    if (null != animations) transaction.setCustomAnimations(animations[0], animations[1], animations[2], animations[3])
    transaction.block()
    transaction.commitAllowingStateLoss()

}
