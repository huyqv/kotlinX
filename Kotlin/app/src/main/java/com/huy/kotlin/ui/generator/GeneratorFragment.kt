package com.huy.kotlin.ui.generator

import android.os.Bundle
import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.adapter.set
import com.huy.kotlin.base.view.BaseFragment
import com.huy.kotlin.ui.user.User
import com.huy.library.handler.DataReceiver
import com.huy.library.handler.GeneratorObservable
import kotlinx.android.synthetic.main.fragment_generator.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class GeneratorFragment : BaseFragment(), DataReceiver<User> {

    private val adapter = UserAdapter()

    private lateinit var dataGenerator: UserGenerator

    override fun layoutResource() = R.layout.fragment_generator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UserProvider.init()
        recyclerView.set(adapter)
        dataGenerator = UserGenerator(this)
        lifecycle.addObserver(GeneratorObservable(dataGenerator))
        appBarView.endButtonClickListener {
            if (dataGenerator.isGenerating()) {
                appBarView.drawableEnd = R.drawable.ic_play
                dataGenerator.pause()
            } else {
                appBarView.drawableEnd = R.drawable.ic_pause
                dataGenerator.play()
            }
        }
    }

    override fun onDataReceived(data: User) {
        adapter.add(data)
        if (adapter.dataNotEmpty()) {
            recyclerView?.smoothScrollToPosition(adapter.lastPosition())
        }
    }

}