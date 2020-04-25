package com.huy.kotlin.ui.recycler.paged

import android.os.Bundle
import com.huy.kotlin.R
import com.huy.kotlin.base.arch.ArchFragment
import com.huy.kotlin.ui.zoom.ZoomFragment
import kotlinx.android.synthetic.main.fragment_adapter_paged.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class PagingFragment : ArchFragment<PagingVM>() {

    private val adapter = PagingAdapter()

    override val layoutResource = R.layout.fragment_adapter_paged

    override val viewModelClass = PagingVM::class.java

    override fun onCreated(state: Bundle?) {
        adapter.myId = 16
        adapter.bind(recyclerView) {
            reverseLayout = true
        }
        adapter.onItemClick = { model, _ ->
            add(ZoomFragment.newInstance(model))
        }
    }

    override fun onRegisterLiveData() {
        viewModel.liveData.observe {
            if (it.isNullOrEmpty()) adapter.hideFooter()
            adapter.submitList(it)
        }
    }

}