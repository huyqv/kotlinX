package com.huy.kotlin.network.callback

import com.huy.kotlin.data.observable.EventLiveData
import com.huy.kotlin.network.rest.onRequestCompleted
import com.huy.kotlin.network.rest.onRequestStarted
import com.huy.kotlin.network.rest.onRequestSuccess
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/11/24
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ApiCallBackLiveData<T> : EventLiveData<T>, Callback<T> {

    private val tag: String?

    open fun hasProgress(): Boolean = true

    constructor(tag: String? = null) {
        this.tag = tag
        onRequestStarted(tag, hasProgress())
    }

    override fun onFailure(call: Call<T>, throwable: Throwable) {
        onRequestCompleted(tag, hasProgress())
        onRequestCompleted(throwable)
    }

    override fun onResponse(call: Call<T>, response: Response<T?>) {
        onRequestCompleted(tag, hasProgress())
        onRequestSuccess(response) { _, _, body ->
            postValue(body)
        }
    }

}