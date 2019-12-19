package com.huy.kotlin.ui.image

import android.os.Bundle
import com.huy.kotlin.R
import com.huy.kotlin.base.arch.ArchFragment
import com.huy.kotlin.ui.image_owner.ImageOwnerFragment
import com.huy.library.extension.onRefresh
import kotlinx.android.synthetic.main.fragment_images.*


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/10/03
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ImageFragment : ArchFragment<ImageVM>() {

    private val adapter = ImageAdapter()

    override val layoutResource = R.layout.fragment_images

    override fun viewModelClass() = ImageVM::class.java

    override fun onCreated(state: Bundle?) {

        adapter.bind(recyclerView, 3)
        adapter.onItemClick { image, _ ->
            add(ImageOwnerFragment.newInstance(adapter.currentList, image))
        }
        swipeRefreshLayout.onRefresh {
            viewModel.fetchImages(0)
        }
        /*adapter.onFooterIndexChange { _, i ->
            viewModel.fetchImages(i / 10 + 1)
        }*/


    }

    override fun onRegisterLiveData() {

        viewModel.fetchImages(0)

        viewModel.imageLiveData.observe {
            adapter.set(it)
            swipeRefreshLayout.isRefreshing = false
        }

    }

}