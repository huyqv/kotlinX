package com.example.kotlin.ui.diff

import android.view.View
import com.example.kotlin.R
import com.example.kotlin.ui.model.Image
import com.example.kotlin.util.load
import com.example.library.adapter.recycler.BaseListAdapter
import com.example.library.util.Anim
import kotlinx.android.synthetic.main.item_image.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/10/04
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class AsyncDiffAdapter : BaseListAdapter<Image>(Image.ItemCallback()) {

    private var lastAnimPosition: Int = -1

    override var blankLayoutResource = R.layout.item_blank

    override var footerLayoutResource = R.layout.item_footer

    override fun layoutResource(model: Image, position: Int) = R.layout.item_image

    override fun View.onBindModel(model: Image, position: Int, layout: Int) {
        if (position > lastAnimPosition) {
            lastAnimPosition = position
            imageView.startAnimation(Anim.fadeIn(1200))
        }
        imageView.load(model.imageUrl) {
            placeholder(R.mipmap.img_placeholder)
            error(R.mipmap.img_placeholder)
        }
    }

}