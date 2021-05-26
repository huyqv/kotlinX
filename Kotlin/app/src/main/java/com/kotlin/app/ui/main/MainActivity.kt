package com.kotlin.app.ui.main

import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.library.extension.listenKeyboard
import com.kotlin.app.R
import com.kotlin.app.ui.base.BaseActivity
import com.kotlin.app.ui.dialog.DialogVM

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/29
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class MainActivity : BaseActivity(), MainView {

    override val mainActivity: MainActivity? get() = this

    override val mainVM by lazy { activityVM(MainVM::class) }

    override val dialogVM by lazy { activityVM(DialogVM::class) }

    override val navController: NavController? by lazy {
        findNavController(R.id.mainFragment)
    }

    override fun layoutResource(): Int {
        return R.layout.main
    }

    override fun onViewCreated() {
        listenKeyboard()
    }

    override fun onLiveDataObserve() {

    }


}






