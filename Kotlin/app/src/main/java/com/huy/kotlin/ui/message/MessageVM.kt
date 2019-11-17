package com.huy.kotlin.ui.message

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.huy.kotlin.base.arch.BaseViewModel
import com.huy.kotlin.data.observable.ToastLiveData
import com.huy.kotlin.network.rest.RestClient
import com.huy.kotlin.network.rest.onNext

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/01/17
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class MessageVM : BaseViewModel() {

    lateinit var messageLiveData: LiveData<PagedList<Message>?>

    override fun onStart() {

        val config = PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)
                .setEnablePlaceholders(true)
                .build()

        messageLiveData = LivePagedListBuilder(DataSourceFactory(), config)
                .setInitialLoadKey(1)
                .build()


    }

    class DataSourceFactory : DataSource.Factory<Int, Message>() {
        override fun create(): DataSource<Int, Message> {
            return MessageDataSource()
        }
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