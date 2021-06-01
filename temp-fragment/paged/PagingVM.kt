package com.kotlin.sample.ui.paged

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.kotlin.sample.base.vm.BaseViewModel
import com.kotlin.sample.data.api.apiClient
import com.kotlin.sample.data.observable.ArchSingleObserver
import com.kotlin.sample.ui.model.Message

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/01/17
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class PagingVM : BaseViewModel() {

    private val pagingConfig: PagedList.Config
        get() = PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(10)
                .setEnablePlaceholders(true)
                .build()

    lateinit var liveData: LiveData<PagedList<Message>?>

    override fun onStart() {
        val factory = object : DataSource.Factory<Int, Message>() {
            override fun create(): DataSource<Int, Message> = MessageDataSource()
        }
        liveData = LivePagedListBuilder(factory, pagingConfig).build()
    }

    override fun onNetworkAvailable() {
        liveData.value?.dataSource?.invalidate()
    }

    class MessageDataSource : PageKeyedDataSource<Int, Message>() {

        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Message>) {
            apiClient.messages(1)
                    .subscribe(object : ArchSingleObserver<List<Message>>() {
                        override fun onResponse(data: List<Message>) {
                            callback.onResult(data, null, 2)
                        }
                    })
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Message>) {
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Message>) {
            apiClient.messages(params.key)
                    .subscribe(object : ArchSingleObserver<List<Message>>() {
                        override fun onResponse(data: List<Message>) {
                            callback.onResult(data, params.key + 1)
                        }
                    })
        }

    }
}