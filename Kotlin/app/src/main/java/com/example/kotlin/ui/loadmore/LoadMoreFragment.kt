package com.example.kotlin.ui.loadmore

import com.example.kotlin.R
import com.example.kotlin.base.arch.ArchFragment
import com.example.library.adapter.recycler.bind
import kotlinx.android.synthetic.main.loadmore.*

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

    override fun localViewModelClass(): Class<LoadMoreVM> {
        return LoadMoreVM::class.java
    }

    override fun onViewCreated() {
        adapter.bind(recyclerView, 3)
        adapter.onFooterIndexChanged = { i ->
            localVM.fetchUsers(i / 10 + 1)
        }
    }

    override fun onRegisterLiveData() {
        localVM.fetchUsers(1)
        localVM.userLiveData.observe {
            adapter.add(it)
        }
    }


}