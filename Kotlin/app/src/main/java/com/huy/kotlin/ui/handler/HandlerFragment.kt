package com.huy.kotlin.ui.handler

import android.os.Bundle
import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.view.BaseFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_handler.*
import java.util.concurrent.TimeUnit


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class HandlerFragment : BaseFragment() {

    private val adapter = HandlerAdapter()

    private var disposable: Disposable? = null

    override val layoutResource = R.layout.fragment_handler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataProvider.init()
        adapter.bind(recyclerView)
        appBarView.endButtonClickListener {
            if (disposable == null || disposable!!.isDisposed) {
                appBarView.drawableEnd = R.drawable.ic_play
                generateUser()
            } else {
                appBarView.drawableEnd = R.drawable.ic_pause
                disposable?.dispose()
            }
        }
    }

    private fun generateUser() {
        disposable = Observable
                .interval(0, 100, TimeUnit.MILLISECONDS)
                .map { DataProvider.random }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adapter.add(it)
                    if (adapter.dataNotEmpty) {
                        recyclerView?.smoothScrollToPosition(adapter.lastPosition)
                    }
                }
    }

}