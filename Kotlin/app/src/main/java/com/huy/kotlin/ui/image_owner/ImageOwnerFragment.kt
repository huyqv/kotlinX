package com.huy.kotlin.ui.image_owner

import android.os.Bundle
import android.view.View
import com.huy.kotlin.BuildConfig
import com.huy.kotlin.R
import com.huy.kotlin.base.adapter.BasePagerAdapter
import com.huy.kotlin.base.view.BaseFragment
import com.huy.kotlin.extension.load
import com.huy.library.extension.SimplePageChangeListener
import com.huy.library.extension.gone
import kotlinx.android.synthetic.main.fragment_image_view.*
import kotlinx.android.synthetic.main.item_zoom_image.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ImageOwnerFragment : BaseFragment(), SimplePageChangeListener {

    private lateinit var adapter: ZoomImageAdapter

    override fun layoutResource() = R.layout.fragment_image_view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (adapter.size() < 2) {
            imageView_textView_count.gone()
            imageView_textView_index.gone()
        } else {
            imageView_textView_count.text = " / ${adapter.size()}"
            imageView_textView_index.text = "1"
            imageView_viewPager.addOnPageChangeListener(this)
        }

        imageView_viewPager.adapter = adapter
        imageView_view_back.setOnClickListener { popBackStack() }
        imageView_viewPager.currentItem = adapter.selectedPosition()
    }

    override fun onPageSelected(position: Int) {
        imageView_textView_index.text = (position + 1).toString()
    }

    companion object {

        fun newInstance(data: List<ImageOwner>, imageOwner: ImageOwner? = null): ImageOwnerFragment {
            require(data.isNotEmpty()) { "ImageOwnerFragment - Data must be not null or empty" }
            return ImageOwnerFragment().apply {
                adapter = ZoomImageAdapter()
                adapter.set(data)
                adapter.selectedItem = imageOwner
            }
        }

        fun newInstance(imageOwner: ImageOwner): ImageOwnerFragment {
            return ImageOwnerFragment().apply {
                adapter = ZoomImageAdapter()
                adapter.set(imageOwner)
            }
        }
    }

    internal class ZoomImageAdapter : BasePagerAdapter<ImageOwner>() {

        override fun layoutRes() = R.layout.item_zoom_image

        override fun View.onBind(model: ImageOwner) {
            zoomImageView.load("${BuildConfig.SERVICE_URL}${model.getImageUrl()}")
        }
    }
}