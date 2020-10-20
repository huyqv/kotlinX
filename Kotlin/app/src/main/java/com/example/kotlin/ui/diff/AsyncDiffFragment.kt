package com.example.kotlin.ui.diff

import com.example.kotlin.R
import com.example.kotlin.base.arch.ArchFragment
import com.example.kotlin.ui.zoom.ZoomFragment
import com.example.kotlin.widget.onRefresh
import com.example.library.adapter.recycler.bind
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

    override fun localViewModelClass(): Class<AsyncDiffVM> {
        return AsyncDiffVM::class.java
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

    override fun onRegisterLiveData() {

        localVM.imageLiveData.observe {
            swipeRefreshLayout.isRefreshing = false
            if (it.isNullOrEmpty()) adapter.hideFooter()
            adapter.set(it)
        }

    }

}