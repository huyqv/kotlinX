package com.huy.kotlin.ui.recycler.paged

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.huy.kotlin.base.arch.BaseViewModel
import com.huy.kotlin.data.observable.ToastLiveData
import com.huy.kotlin.extension.PAGED_DEFAULT_CONFIG
import com.huy.kotlin.network.rest.RestClient
import com.huy.kotlin.network.rest.onNext
import com.huy.kotlin.ui.model.Message

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

            RestClient.service.messages(1).onNext { code, message, body ->
                if (!body.isNullOrEmpty()) {
                    callback.onResult(body, null, 2)
                } else {
                    ToastLiveData.message = "$code - $message"
                }
            }
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Message>) {
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Message>) {

            RestClient.service.messages(params.key).onNext { code, message, body ->
                if (!body.isNullOrEmpty()) {
                    callback.onResult(body, params.key + 1)
                } else {
                    ToastLiveData.message = "$code - $message"
                }
            }
        }

    }
}