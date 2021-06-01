package com.kotlin.sample.ui.paged

import com.kotlin.sample.R
import com.kotlin.sample.base.arch.ArchFragment
import com.kotlin.sample.ui.zoom.ZoomFragment
import kotlinx.android.synthetic.main.fragment_adapter_paged.*
import kotlin.reflect.KClass

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

    override fun layoutResource(): Int {
        return R.layout.sample_adapter_paged
    }

    override fun localViewModelClass(): KClass<PagingVM> {
        return PagingVM::class
    }

    override fun onViewCreated() {
        adapter.myId = 16
        adapter.bind(recyclerView) {
            reverseLayout = true
        }
        adapter.onItemClick = { model, _ ->
            add(ZoomFragment.newInstance(model))
        }
    }

    override fun onLiveDataObserve() {
        localVM.liveData.observe {
            if (it.isNullOrEmpty()) adapter.hideFooter()
            adapter.submitList(it)
        }
    }


}