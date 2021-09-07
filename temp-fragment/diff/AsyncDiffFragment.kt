package com.kotlin.sample.diff

import com.kotlin.sample.R
import com.kotlin.sample.base.arch.ArchFragment
import com.kotlin.sample.ui.zoom.ZoomFragment
import com.kotlin.sample.widget.onRefresh
import kotlinx.android.synthetic.main.fragment_adapter_async_diff.*
import kotlin.reflect.KClass

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/10/03
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class AsyncDiffFragment : ArchFragment<AsyncDiffVM>() {

    private val adapter = AsyncDiffAdapter()

    override fun layoutResource(): Int {
        return R.layout.sample_adapter_async_diff
    }

    override fun localViewModelClass(): KClass<AsyncDiffVM> {
        return AsyncDiffVM::class
    }

    override fun onViewCreated() {
        adapter.bind(recyclerView, 3)
        adapter.onItemClick = { image, _ ->
            add(ZoomFragment.newInstance(adapter.currentList, image))
        }
        swipeRefreshLayout.onRefresh {
            localVM.fetchImages(0)
        }
    }

    override fun onLiveDataObserve() {

        localVM.imageLiveData.observe {
            swipeRefreshLayout.isRefreshing = false
            if (it.isNullOrEmpty()) adapter.hideFooter()
            adapter.set(it)
        }

    }

}