package com.huy.kotlin.ui.recycler.paged

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.huy.kotlin.base.arch.BaseViewModel
import com.huy.kotlin.data.api.ApiClient
import com.huy.kotlin.data.observable.ArchSingleObserver
import com.huy.kotlin.ui.model.Message
import com.huy.kotlin.util.PAGED_DEFAULT_CONFIG

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/01/17
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class PagingVM : BaseViewModel() {

    lateinit var liveData: LiveData<PagedList<Message>?>

    override fun onStart() {
        val factory = object : DataSource.Factory<Int, Message>() {
            override fun create(): DataSource<Int, Message> = MessageDataSource()
        }
        liveData = LivePagedListBuilder(factory, PAGED_DEFAULT_CONFIG).build()
    }

    override fun onNetworkAvailable() {
        liveData.value?.dataSource?.invalidate()
    }

    class MessageDataSource : PageKeyedDataSource<Int, Message>() {

        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Message>) {
            ApiClient.instance.messages(1)
                    .subscribe(object : ArchSingleObserver<List<Message>>() {
                        override fun onResponse(data: List<Message>) {
                            callback.onResult(data, null, 2)
                        }
                    })
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Message>) {
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Message>) {
            ApiClient.instance.messages(params.key)
                    .subscribe(object : ArchSingleObserver<List<Message>>() {
                        override fun onResponse(data: List<Message>) {
                            callback.onResult(data, params.key + 1)
                        }
                    })
        }

    }
}