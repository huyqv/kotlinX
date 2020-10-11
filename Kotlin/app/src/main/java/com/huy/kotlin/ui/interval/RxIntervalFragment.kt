package com.huy.kotlin.ui.interval

import com.huy.kotlin.R
import com.huy.kotlin.base.view.BaseFragment
import com.huy.library.adapter.recycler.bind
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
class RxIntervalFragment : BaseFragment() {

    private val adapter: RxIntervalAdapter = RxIntervalAdapter()

    private var disposable: Disposable? = null

    override fun layoutResource(): Int {
        return R.layout.fragment_handler
    }

    override fun onViewCreated() {
        adapter.bind(recyclerView)
        appBarView.endButtonClickListener {
            if (disposable == null || disposable!!.isDisposed) {
                textViewPlay.text = "Play"
                generateUser()
            } else {
                textViewPlay.text = "Pause"
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
                        recyclerView?.smoothScrollToPosition(adapter.lastIndex)
                    }
                }
    }

}