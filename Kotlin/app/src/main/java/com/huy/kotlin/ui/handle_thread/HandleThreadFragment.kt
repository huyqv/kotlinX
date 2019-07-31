package com.huy.kotlin.ui.handle_thread

import android.os.Bundle
import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.adapter.set
import com.huy.kotlin.base.view.BaseFragment
import com.huy.kotlin.ui.user.User
import com.huy.library.handler.DataReceiver
import kotlinx.android.synthetic.main.fragment_handle_thread.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class HandleThreadFragment : BaseFragment(), DataReceiver<User> {

    private val adapter = UserAdapter()

    private lateinit var dataGenerator: UserGenerator

    override fun layoutResource() = R.layout.fragment_handle_thread

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UserProvider.init()
        recyclerView.set(adapter)
        appBarView.rightIconClickListener {
            if (dataGenerator.isGenerating()) {
                appBarView.drawableEnd = R.drawable.ic_play
                dataGenerator.pauseGenerate()
            } else {
                appBarView.drawableEnd = R.drawable.ic_pause
                dataGenerator.playGenerate()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dataGenerator = UserGenerator(this)
        dataGenerator.start()
    }

    override fun onStop() {
        dataGenerator.quit()
        super.onStop()
    }

    override fun onDataReceived(data: User) {
        adapter.add(data)
        if (adapter.dataNotEmpty()) {
            recyclerView?.smoothScrollToPosition(adapter.lastPosition())
        }
    }

}