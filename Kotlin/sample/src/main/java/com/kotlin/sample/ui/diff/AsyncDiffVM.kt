package com.kotlin.sample.diff

import androidx.lifecycle.MutableLiveData
import com.example.kotlin.base.vm.BaseViewModel
import com.example.kotlin.data.api.apiClient
import com.example.kotlin.data.observable.ArchSingleObserver
import com.example.kotlin.ui.model.Image
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
        apiClient.images(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ArchSingleObserver<List<Image>>() {
                    override fun onResponse(data: List<Image>) {
                        imageLiveData.postValue(data)
                    }
                })
    }

}