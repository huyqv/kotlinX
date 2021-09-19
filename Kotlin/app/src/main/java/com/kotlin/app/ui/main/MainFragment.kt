package com.kotlin.app.ui.main

import androidx.viewbinding.ViewBinding
import com.kotlin.app.ui.base.BaseFragment
import com.sample.library.extension.activityVM

abstract class MainFragment<B : ViewBinding> : BaseFragment<B>(), MainFragmentView {

    override val mainActivity get() = requireActivity() as? MainActivity

    override val mainVM by activityVM(MainVM::class)

}