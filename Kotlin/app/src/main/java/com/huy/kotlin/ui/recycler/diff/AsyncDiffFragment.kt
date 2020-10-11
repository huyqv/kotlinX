package com.huy.kotlin.ui.recycler.diff

import com.huy.kotlin.R
import com.huy.kotlin.base.arch.ArchFragment
import com.huy.kotlin.ui.zoom.ZoomFragment
import com.huy.kotlin.widget.onRefresh
import kotlinx.android.synthetic.main.fragment_adapter_async_diff.*

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
        return R.layout.fragment_adapter_async_diff
    }

    override val viewModelClass = AsyncDiffVM::class.java

    override fun onViewCreated() {
        adapter.bind(recyclerView, 3)
        adapter.onItemClick = { image, _ ->
            add(ZoomFragment.newInstance(adapter.currentList, image))
        }
        swipeRefreshLayout.onRefresh {
            viewModel.fetchImages(0)
        }
    }

    override fun onRegisterLiveData() {

        viewModel.imageLiveData.observe {
            swipeRefreshLayout.isRefreshing = false
            if (it.isNullOrEmpty()) adapter.hideFooter()
            adapter.set(it)
        }

    }

}