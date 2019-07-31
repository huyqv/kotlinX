package com.huy.kotlin.ui.paging

import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.adapter.BasePagedListAdapter
import kotlinx.android.synthetic.main.item_paging.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class PagingAdapter : BasePagedListAdapter<PagingItem>(PagingItem.DIFF_CALLBACK) {

    override fun footerLayoutResource() = R.layout.item_footer

    override fun layoutResource(model: PagingItem, position: Int) = R.layout.item_paging

    override fun View.onBindModel(model: PagingItem, position: Int, layout: Int) {
        textView.text = model.id.toString()
    }

}