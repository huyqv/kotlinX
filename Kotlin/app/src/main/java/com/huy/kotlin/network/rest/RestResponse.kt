package com.huy.kotlin.network.rest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/07/20
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RestResponse<T> {

    @SerializedName("status")
    var status: Int = 500

    @SerializedName("message")
    @Expose
    var message: String = "Something went wrong :( !"

    @SerializedName("body")
    @Expose
    var body: T? = null

    constructor()

    constructor(throwable: Throwable?) {

        if (throwable == null) {
            return
        }

        if (throwable.isNetworkError()) {
            message = "Network error !"
            return
        }

        throwable.message?.apply { message = this }
    }

    constructor(response: Response<T>?) {

        response ?: return

        status = response.code()

        if (response.isSuccessful) {
            message = response.message()
            body = response.body()
            return
        }

        response.errorBody()?.string()?.apply {
            message = this
        }

    }

    constructor(status: Int, message: String, body: T?) {
        this.status = status
        this.message = message
        this.body = body
    }

    private fun Throwable.isNetworkError(): Boolean {
        return this is SocketException || this is UnknownHostException || this is SocketTimeoutException
    }

    val isSuccessful: Boolean get() = status in 200..299 && body != null

}