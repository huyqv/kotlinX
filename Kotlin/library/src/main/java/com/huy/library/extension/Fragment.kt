package com.huy.library.extension

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
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun Fragment.addFragment(fragment: Fragment, @IdRes container: Int, backStack: Boolean = true) {

    val tag = fragment::class.java.simpleName
    childFragmentManager.scheduleTransaction {
        add(container, fragment, tag)
        if (backStack) addToBackStack(tag)
    }
}

fun Fragment.replaceFragment(fragment: Fragment, @IdRes container: Int, backStack: Boolean = true) {

    val tag = fragment::class.java.simpleName
    childFragmentManager.scheduleTransaction {
        replace(container, fragment, tag)
        if (backStack) addToBackStack(tag)
    }
}

fun Fragment.remove(cls: Class<Fragment>) {

    val fragment = childFragmentManager.findFragmentByTag(cls.simpleName) ?: return
    childFragmentManager.scheduleTransaction {
        remove(fragment)
    }
}

fun FragmentActivity.addFragment(fragment: Fragment, @IdRes container: Int, backStack: Boolean = true) {

    val tag = fragment::class.java.simpleName
    supportFragmentManager.scheduleTransaction {
        add(container, fragment, tag)
        if (backStack) addToBackStack(tag)

    }
}

fun FragmentActivity.replaceFragment(fragment: Fragment, @IdRes container: Int, backStack: Boolean = true) {

    val tag = fragment::class.java.simpleName
    supportFragmentManager.scheduleTransaction {
        replace(container, fragment, tag)
        if (backStack) addToBackStack(tag)
    }
}

fun FragmentActivity.remove(cls: Class<*>) {

    val fragment = supportFragmentManager.findFragmentByTag(cls.simpleName) ?: return
    supportFragmentManager.scheduleTransaction {
        remove(fragment)
    }
}

fun FragmentActivity.clearStack() {
    val sfm = this.supportFragmentManager
    for (fragment in sfm.fragments) {
        if (fragment !is Fragment) continue
        sfm.beginTransaction().remove(fragment).commit()
    }
    sfm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun FragmentManager.scheduleTransaction(block: FragmentTransaction.() -> Unit) {

    val transaction = beginTransaction()
    transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
    transaction.block()
    transaction.commitAllowingStateLoss()

}
