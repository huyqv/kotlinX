package com.kotlin.app.data.network

import com.example.library.extension.toast
import com.google.gson.JsonObject
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/07/20
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class ApiObserver<T> : DisposableObserver<T>() {


    /**
     * [DisposableObserver] implement
     */
    final override fun onStart() {
        // show progress
    }

    final override fun onComplete() {
        if (!isDisposed) this.dispose()
        // hide progress
    }

    final override fun onNext(t: T) {
        onCompleted(t, null)
        onResponse(t)
    }

    final override fun onError(e: Throwable) {
        onCompleted(null, e)
        when (e) {
            is HttpException -> onFailed(e.code(), e.message(), e.errorResponse)
            is SocketException, is UnknownHostException, is SocketTimeoutException -> onNetworkError()
            else -> onFailed(0, e.message, null)
        }
    }


    /**
     * Open callback wrapper methods
     */
    protected open fun onCompleted(data: T?, e: Throwable?) {
    }

    protected open fun onResponse(data: T) {}

    protected open fun onFailed(code: Int, message: String?, body: JsonObject?) {
        toast("$code $message")
    }

    protected open fun onNetworkError() {
        onFailed(0, "network error", null)
    }

}