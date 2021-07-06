package com.kotlin.app.ui.main

import androidx.viewbinding.ViewBinding
import com.example.library.extension.activityVM
import com.kotlin.app.ui.base.BaseFragment

abstract class MainFragment<B : ViewBinding> : BaseFragment<B>(), MainView {

    override val mainActivity get() = requireActivity() as? MainActivity

    override val mainVM by activityVM(MainVM::class)

}