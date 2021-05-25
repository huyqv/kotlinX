package com.kotlin.app.ui.main

import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.library.extension.listenKeyboard
import com.example.library.extension.post
import com.kotlin.app.BuildConfig
import com.kotlin.app.MainDirections
import com.kotlin.app.R
import com.kotlin.app.ui.base.BaseActivity
import com.kotlin.app.ui.dialog.DialogVM
import kotlinx.android.synthetic.main.main.*

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
        textViewVersion.text = "${BuildConfig.VERSION_NAME}-${BuildConfig.VERSION_CODE}"
        listenKeyboard()
        post(1000) {
            navigate(MainDirections.actionGlobalIntroFragment())
        }
        post(2000) {
            navigate(MainDirections.actionGlobalHomeFragment())
        }
    }

    override fun onLiveDataObserve() {
    }

}






