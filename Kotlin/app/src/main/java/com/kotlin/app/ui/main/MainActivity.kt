package com.kotlin.app.ui.main

import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.kotlin.app.R
import com.kotlin.app.databinding.MainBinding
import com.kotlin.app.ui.base.BaseActivity
import com.sample.library.extension.lazyViewModel

class MainActivity : BaseActivity<MainBinding>() {

    private val mainVM by lazyViewModel(MainVM::class)

    override fun activityNavController(): NavController {
        return findNavController(R.id.fragment)
    }

    override fun inflating(): (LayoutInflater) -> ViewBinding {
        return MainBinding::inflate
    }

    override fun onViewCreated() {
        bind.statusBarView.observer(this)
        bind.keyboardView.observer(this)
    }

    override fun onLiveDataObserve() {

    }

    fun setBackground(@ColorInt color: Int) {
        bind.layoutContent.setBackgroundColor(color)
    }

}






