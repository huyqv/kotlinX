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

    private val progression: Boolean

    constructor(tag: String? = null, progression: Boolean = false) {
        this.tag = tag
        this.progression = progression
        onRequestStarted(tag, progression)
    }

    override fun onFailure(call: Call<T>, throwable: Throwable) {
        onRequestCompleted(tag, progression)
        onRequestCompleted(throwable)
    }

    override fun onResponse(call: Call<T>, response: Response<T?>) {
        onRequestCompleted(tag, progression)
        onRequestSuccess(response) { _, _, body ->
            postValue(body)
        }
    }

}