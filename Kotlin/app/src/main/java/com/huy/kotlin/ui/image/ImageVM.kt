package com.huy.kotlin.ui.image

import com.huy.kotlin.base.arch.BaseViewModel
import com.huy.kotlin.data.observable.SingleLiveData
import com.huy.kotlin.network.rest.RestClient
import com.huy.kotlin.network.rest.onNext
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
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



}