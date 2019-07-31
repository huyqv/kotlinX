package com.huy.kotlin.ui.animation

import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.adapter.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.item_text.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/07/12
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class AnimationAdapter : BaseRecyclerAdapter<String>() {

    override fun layoutResource(model: String, position: Int) = R.layout.item_text

    override fun View.onBindModel(model: String, position: Int, layout: Int) {
        itemTextView.text = model
    }

}