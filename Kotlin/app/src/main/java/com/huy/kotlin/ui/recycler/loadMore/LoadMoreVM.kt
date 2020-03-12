package com.huy.kotlin.ui.recycler.loadMore

import com.huy.kotlin.base.arch.BaseViewModel
import com.huy.kotlin.data.observable.SingleLiveData
import com.huy.kotlin.network.RestClient
import com.huy.kotlin.network.callback.ArchSingleObserver
import com.huy.kotlin.ui.model.User
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io

class LoadMoreVM : BaseViewModel() {

    val userLiveData = SingleLiveData<List<User>>()

    override fun onStart() {
    }

    fun fetchUsers(page: Int) {
        RestClient.instance.users(page)
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe(object : ArchSingleObserver<List<User>>() {
                    override fun onResponse(data: List<User>) {
                        userLiveData.postValue(data)
                    }
                })
    }
}