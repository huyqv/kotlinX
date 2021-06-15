package com.kotlin.app.ui.zoom
/*
import android.view.View
import com.example.library.adapter.fragment.ViewPagerAdapter
import com.example.library.extension.SimplePageChangeListener
import com.kotlin.app.R

import template.ui.BaseFragment


class ZoomFragment : BaseFragment(), SimplePageChangeListener {

    private lateinit var adapter: ZoomImageAdapterView

    override fun layoutResource(): Int {
        return R.layout.image
    }

    override fun onLiveDataObserve() {
    }

    override fun onViewCreated() {
        if (adapter.size < 2) {
            imageView_textView_count.gone()
            imageView_textView_index.gone()
        } else {
            imageView_textView_count.text = " / ${adapter.size}"
            imageView_textView_index.text = "1"
            imageView_viewPager.addOnPageChangeListener(this)
        }

        imageView_viewPager.adapter = adapter
        imageView_view_back.setOnClickListener { onBackPressed() }
        imageView_viewPager.currentItem = adapter.selectedPosition()
    }

    override fun onPageSelected(position: Int) {
        imageView_textView_index.text = (position + 1).toString()
    }

    companion object {

        fun newInstance(data: List<ImageOwner>, imageOwner: ImageOwner): ZoomFragment {
            return ZoomFragment().apply {
                adapter = ZoomImageAdapterView()
                adapter.set(data)
                adapter.selectedItem = imageOwner
            }
        }

        fun newInstance(imageOwner: ImageOwner): ZoomFragment {
            return ZoomFragment().apply {
                adapter = ZoomImageAdapterView()
                adapter.set(imageOwner)
            }
        }
    }

    internal class ZoomImageAdapterView : ViewPagerAdapter<ImageOwner>() {

        override fun layoutRes() = R.layout.item_zoom_image

        override fun View.onBind(model: ImageOwner) {
            zoomImageView.load(model.imageUrl)
        }
    }
}*/
