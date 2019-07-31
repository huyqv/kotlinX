package com.huy.kotlin.ui.image

import com.huy.kotlin.base.arch.BaseViewModel
import com.huy.kotlin.data.observable.SingleLiveData
import com.huy.kotlin.network.rest.RestClient
import com.huy.kotlin.network.rest.onNext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/01/17
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ImageVM : BaseViewModel() {

    val imageLiveData = SingleLiveData<List<Image>>()

    override fun onStart() {
    }

    fun fetchImages(page: Int) {
        RestClient.service.images(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onNext { _, _, list ->
                    imageLiveData.postValue(list)
                }
    }
}