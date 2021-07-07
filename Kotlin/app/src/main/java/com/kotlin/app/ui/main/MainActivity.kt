package com.kotlin.app.ui.main

import android.view.LayoutInflater
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kotlin.app.R
import com.kotlin.app.databinding.MainBinding
import com.kotlin.app.ui.base.BaseActivity
import com.sample.library.extension.viewModel

class MainActivity : BaseActivity<MainBinding>(), MainView {

    override val mainActivity: MainActivity? get() = this

    override val mainVM by viewModel(MainVM::class)

    override fun navController(): NavController? {
        return findNavController(R.id.fragment)
    }

    override fun inflating(): (LayoutInflater) -> MainBinding {
        return MainBinding::inflate
    }

    override fun onViewCreated() {
    }

    override fun onLiveDataObserve() {

    }

}






