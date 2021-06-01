package com.kotlin.sample.ui.loadmore

import com.kotlin.sample.R
import com.kotlin.sample.base.arch.ArchFragment
import com.kotlin.sample.ui.loadmore.LoadMoreAdapter
import com.kotlin.sample.ui.loadmore.LoadMoreVM
import kotlinx.android.synthetic.main.loadmore.*
import kotlin.reflect.KClass

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
        return R.layout.loadmore
    }

    override fun localViewModelClass(): KClass<LoadMoreVM> {
        return LoadMoreVM::class
    }

    override fun onViewCreated() {
        adapter.bind(recyclerView, 3)
        adapter.onFooterIndexChanged = { i ->
            localVM.fetchUsers(i / 10 + 1)
        }
    }

    override fun onLiveDataObserve() {
        localVM.fetchUsers(1)
        localVM.userLiveData.observe {
            adapter.add(it)
        }
    }


}