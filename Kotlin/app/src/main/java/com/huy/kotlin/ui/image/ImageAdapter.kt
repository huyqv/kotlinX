package com.huy.kotlin.ui.image

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.huy.kotlin.R
import com.huy.kotlin.base.adapter.BaseRecyclerAdapter
import com.huy.kotlin.extension.loadImage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_image.view.*


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/10/04
 * @Description: ...
 * None Right Reserved
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

    fun observe(observable: Observable<MutableList<Image>>): Disposable {
        var newList: MutableList<Image> = mutableListOf()
        return observable
                .doOnNext { newList = it }
                .map { DiffUtil.calculateDiff(Image.DiffCallback(data, it)) }
                .observeOn(mainThread())
                .doOnNext {
                    data.clear()
                    data.addAll(newList)
                }
                .subscribe {
                    it.dispatchUpdatesTo(this)
                }
    }


}