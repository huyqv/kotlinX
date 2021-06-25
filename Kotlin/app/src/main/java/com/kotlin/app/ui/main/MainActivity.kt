package com.kotlin.app.ui.main

import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.library.extension.viewModel
import com.kotlin.app.R
import com.kotlin.app.databinding.MainBinding
import com.kotlin.app.ui.base.BaseActivity

class MainActivity : BaseActivity<MainBinding>(MainBinding::inflate), MainView {

    override val mainActivity: MainActivity? get() = this

    override val mainVM by viewModel(MainVM::class)

    override fun navController(): NavController? {
        return findNavController(R.id.fragment)
    }

    override fun onViewCreated() {
    }

    override fun onLiveDataObserve() {

    }

}






