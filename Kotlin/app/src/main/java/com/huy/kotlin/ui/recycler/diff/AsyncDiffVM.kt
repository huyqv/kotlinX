package com.huy.kotlin.ui.recycler.diff

import androidx.lifecycle.MutableLiveData
import com.huy.kotlin.base.arch.BaseViewModel
import com.huy.kotlin.network.RestClient
import com.huy.kotlin.network.callback.ArchSingleObserver
import com.huy.kotlin.ui.model.Image
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/01/17
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class AsyncDiffVM : BaseViewModel() {

    val imageLiveData = MutableLiveData<List<Image>>()

    override fun onStart() {

    }

    override fun onNetworkAvailable() {
        fetchImages(0)
    }

    fun fetchImages(page: Int) {
        RestClient.instance.images(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ArchSingleObserver<List<Image>>() {
                    override fun onResponse(data: List<Image>) {
                        imageLiveData.postValue(data)
                    }
                })
    }

}