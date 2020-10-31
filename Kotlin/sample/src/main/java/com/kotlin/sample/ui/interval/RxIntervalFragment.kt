package com.kotlin.sample.interval

import com.example.kotlin.R
import com.example.kotlin.base.view.BaseFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


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

    private val counter = AtomicInteger()

    override fun layoutResource(): Int {
        return R.layout.sample_interval
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

    override fun onLiveDataObserve() {
    }

    private fun generateUser() {
        disposable = Observable
                .interval(0, 300, TimeUnit.MILLISECONDS)
                .map { counter.incrementAndGet() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adapter.add(it)
                    if (adapter.dataNotEmpty) {
                        recyclerView?.smoothScrollToPosition(adapter.lastIndex)
                    }
                }
    }

}