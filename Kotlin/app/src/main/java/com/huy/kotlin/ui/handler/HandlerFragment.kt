package com.huy.kotlin.ui.handler

import android.os.Bundle
import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.view.BaseFragment
import com.huy.kotlin.ui.model.User
import com.huy.library.thread.DataReceiver
import com.huy.library.thread.ThreadObservable
import kotlinx.android.synthetic.main.fragment_handler.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class HandlerFragment : BaseFragment(), DataReceiver {

    private val adapter = HandlerAdapter()

    private lateinit var dataGenerator: DataThread

    override val layoutResource = R.layout.fragment_handler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataProvider.init()
        adapter.bind(recyclerView)
        dataGenerator = DataThread().also { it.receiver = this }
        lifecycle.addObserver(ThreadObservable(dataGenerator))
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

    override fun onDataReceived(thread: String, data: Any) {
        adapter.add(data as User)
        if (adapter.dataNotEmpty) {
            recyclerView?.smoothScrollToPosition(adapter.lastPosition)
        }
    }

}