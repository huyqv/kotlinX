package com.kotlin.app.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2021/05/25
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface FragmentView : BaseView {

    val fragment: Fragment

    override val baseActivity: BaseActivity? get() = fragment.requireActivity() as? BaseActivity

    override val lifecycleOwner: LifecycleOwner get() = fragment.viewLifecycleOwner

    override val navController: NavController? get() = fragment.findNavController()

    override fun add(fragment: Fragment, stack: Boolean) {
        baseActivity?.add(fragment, stack)
    }

    override fun replace(fragment: Fragment, stack: Boolean) {
        baseActivity?.replace(fragment, stack)
    }

    override fun <T : Fragment> remove(cls: Class<T>) {
        baseActivity?.remove(cls)
    }

}