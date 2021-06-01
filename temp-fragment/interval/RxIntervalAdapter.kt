package com.kotlin.sample.interval

import android.view.View
import com.kotlin.sample.R
import com.example.library.adapter.recycler.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.interval_item.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RxIntervalAdapter : BaseRecyclerAdapter<Int>() {

    override var blankLayoutResource = R.layout.sample_adapter_item_blank

    override var footerLayoutResource = R.layout.sample_adapter_item_footer

    override fun layoutResource(model: Int, position: Int) = R.layout.sample_interval_item

    override fun View.onBindModel(model: Int, position: Int, layout: Int) {
        intervalTextView.text = model.toString()
    }

}