package com.kotlin.app.ui.main

import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kotlin.app.R
import com.kotlin.app.ui.dialog.DialogVM
import template.ui.BaseActivity

class MainActivity : BaseActivity(), MainView {

    override val mainActivity: MainActivity? get() = this

    override val mainVM by lazy { viewModel(MainVM::class) }

    override val dialogVM by lazy { viewModel(DialogVM::class) }

    override val navController: NavController? by lazy {
        findNavController(R.id.mainFragment)
    }

    override fun layoutResource(): Int {
        return R.layout.main
    }

    override fun onViewCreated() {
    }

    override fun onLiveDataObserve() {

    }


}






