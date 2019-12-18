package com.huy.kotlin.ui.image

import androidx.recyclerview.widget.DiffUtil
import com.huy.kotlin.base.arch.BaseViewModel
import com.huy.kotlin.data.observable.SingleLiveData
import com.huy.kotlin.network.rest.RestClient
import com.huy.kotlin.network.rest.onNext
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/01/17
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ImageVM : BaseViewModel() {

    val imageLiveData = SingleLiveData<List<Image>>()

    override fun onStart() {
    }

    fun fetchImages(page: Int) {
        RestClient.service.images(page)
                .subscribeOn(io())
                .observeOn(mainThread())
                .onNext { _, _, list ->
                    imageLiveData.postValue(list)
                }
    }


    val diffLiveData = SingleLiveData<DiffUtil.DiffResult>()

    fun fetchImages(page: Int, data: MutableList<Image>): Disposable {
        var newList: MutableList<Image>? = null
        return RestClient.service.images(page)
                .subscribeOn(io())
                .observeOn(mainThread())
                .doOnNext {
                    newList = it.toMutableList()
                }
                .map {
                    DiffUtil.calculateDiff(Image.DiffCallback(data, it), true)
                }
                .doOnNext {
                    newList ?: return@doOnNext
                    data.clear()
                    data.addAll(newList!!)
                }
                .onNext { _, _, it ->
                    diffLiveData.postValue(it)
                }
    }

}