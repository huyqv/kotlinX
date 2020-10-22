package com.example.kotlin.ui.loadmore

import com.example.kotlin.R
import com.example.kotlin.base.arch.ArchFragment
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