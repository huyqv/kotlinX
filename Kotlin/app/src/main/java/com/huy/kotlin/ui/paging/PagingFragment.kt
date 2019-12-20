package com.huy.kotlin.ui.paging

import android.os.Bundle
import com.huy.kotlin.R
import com.huy.kotlin.base.adapter.set
import com.huy.kotlin.base.arch.ArchFragment
import kotlinx.android.synthetic.main.fragment_paging.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/11
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class PagingFragment : ArchFragment<PagingVM>() {

    private var adapter = PagingAdapter()

    override val layoutResource = R.layout.fragment_paging

    override val viewModelClass = PagingVM::class.java

    override fun onCreated(state: Bundle?) {
        recyclerView.set(adapter)
    }

    override fun onRegisterLiveData() {
        viewModel.liveData(1).observe {
            adapter.submitList(it)
        }
    }

}