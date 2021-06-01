package com.kotlin.sample.ui.loadmore

import com.kotlin.sample.base.ext.SingleLiveData
import com.kotlin.sample.base.vm.BaseViewModel
import com.kotlin.sample.data.api.apiClient
import com.kotlin.sample.data.observable.ArchSingleObserver
import com.kotlin.sample.ui.model.User
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io

class LoadMoreVM : BaseViewModel() {

    val userLiveData = SingleLiveData<List<User>>()

    override fun onStart() {
    }

    fun fetchUsers(page: Int) {
        apiClient.users(page)
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe(object : ArchSingleObserver<List<User>>() {
                    override fun onResponse(data: List<User>) {
                        userLiveData.postValue(data)
                    }
                })
    }
}