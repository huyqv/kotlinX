package com.huy.kotlin.ui.recycler.loadMore

import com.huy.kotlin.R
import com.huy.kotlin.base.arch.ArchFragment
import kotlinx.android.synthetic.main.fragment_adapter_load_more.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class LoadMoreFragment : ArchFragment<LoadMoreVM>() {

    private val adapter = LoadMoreAdapter()

    override fun layoutResource(): Int {
        return R.layout.fragment_adapter_load_more
    }

    override val viewModelClass = LoadMoreVM::class.java

    override fun onViewCreated() {
        adapter.bind(recyclerView, 3)
        adapter.onFooterIndexChange = { _, i ->
            viewModel.fetchUsers(i / 10 + 1)
        }
    }

    override fun onRegisterLiveData() {
        viewModel.fetchUsers(1)
        viewModel.userLiveData.observe {
            adapter.add(it)
        }
    }


}