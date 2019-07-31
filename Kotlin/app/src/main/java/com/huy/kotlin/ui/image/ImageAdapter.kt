package com.huy.kotlin.ui.image

import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.adapter.BaseRecyclerAdapter
import com.huy.kotlin.extension.loadImage
import kotlinx.android.synthetic.main.item_image.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/10/04
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ImageAdapter : BaseRecyclerAdapter<Image>() {

    override fun blankLayoutResource(): Int {
        return R.layout.item_blank
    }

    override fun footerLayoutResource(): Int {
        return R.layout.item_footer
    }

    override fun layoutResource(model: Image, position: Int) = R.layout.item_image

    override fun View.onBindModel(model: Image, position: Int, layout: Int) {
        imageView.loadImage(model.url)
    }

}