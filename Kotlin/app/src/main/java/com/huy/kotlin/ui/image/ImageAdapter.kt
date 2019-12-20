package com.huy.kotlin.ui.image

import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.adapter.BaseListAdapter
import com.huy.library.util.Anim
import kotlinx.android.synthetic.main.item_image.view.*


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/10/04
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ImageAdapter : BaseListAdapter<Image>(Image.ItemCallback()) {

    override var blankLayoutResource = R.layout.item_blank

    override var footerLayoutResource = R.layout.item_footer

    override fun layoutResource(model: Image, position: Int) = R.layout.item_image

    override fun View.onFirstBindModel(model: Image, position: Int, layout: Int) {
        imageView.startAnimation(Anim.fadeIn(1200))
        onBindModel(model, position, layout)
    }

    override fun View.onBindModel(model: Image, position: Int, layout: Int) {
        model.bind(imageView)
    }

}