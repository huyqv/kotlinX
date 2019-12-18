package com.huy.kotlin.network.rest

import com.huy.kotlin.data.observable.ProgressLiveData
import com.huy.library.extension.toast
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import org.reactivestreams.Subscription
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/07/21
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface ResponseListener<T> {

    fun hasProgress(): Boolean {
        return true
    }

    fun onUnknownError() {}

    fun onNetworkError() {
        toast("network error")
    }

    fun onSuccess(response: T)

    fun onCompleted(code: Int, message: String, response: T?) {
        if (null != response) onSuccess(response)
    }

    fun onFailure(code: Int, message: String) {}

    fun onError(code: Int, message: String, response: Any?) {
        onFailure(code, message)
    }

    fun onResponse(response: Response<T?>?) {

        if (response == null) {
            onError(null)
            return
        }

        if (response.isSuccessful) {
            onCompleted(response.code(), response.message(), response.body())
            return
        }

        if (response.errorBody() == null) {
            onRequestCompleted()
            return
        }

        onError(response.code(), response.message(), response.errorBody())

    }

    fun onError(throwable: Throwable?) {

        when (throwable) {

            is HttpException -> onError(throwable.code(), throwable.message(), throwable.response()?.errorBody())

            is SocketException, is UnknownHostException, is SocketTimeoutException -> onNetworkError()

            else -> onUnknownError()
        }
    }

    fun onStart() {
        RestClient.add(null)
        if (hasProgress()) ProgressLiveData.show()
    }

    fun onCompleted() {
        RestClient.remove(null)
        if (hasProgress()) ProgressLiveData.show()
    }

}

fun <T> Observable<Response<T?>>.request(listener: ResponseListener<T?>): Disposable {

    return subscribeWith(object : DisposableObserver<Response<T?>?>() {
        override fun onStart() {
            listener.onStart()
        }

        override fun onNext(response: Response<T?>) {
            listener.onResponse(response)
        }

        override fun onComplete() {
            listener.onCompleted()
        }

        override fun onError(throwable: Throwable) {
            listener.onError(throwable)
        }
    })
}

fun <T> Single<Response<T?>>.request(listener: ResponseListener<T?>) {

    subscribe(object : SingleObserver<Response<T?>> {
        override fun onSubscribe(disposable: Disposable) {
            listener.onStart()
        }

        override fun onSuccess(response: Response<T?>) {
            listener.onCompleted()
            listener.onResponse(response)
        }

        override fun onError(throwable: Throwable) {
            listener.onCompleted()
            listener.onError(throwable)
        }
    })
}

fun <T> Flowable<Response<T?>>.request(listener: ResponseListener<T?>) {

    subscribe(object : FlowableSubscriber<Response<T?>> {
        override fun onSubscribe(subscription: Subscription) {
            listener.onStart()
        }

        override fun onNext(response: Response<T?>) {
            listener.onCompleted()
            listener.onResponse(response)
        }

        override fun onError(throwable: Throwable?) {
            listener.onCompleted()
            listener.onError(throwable)
        }

        override fun onComplete() {
            listener.onCompleted()
        }
    })
}

fun <T> Call<T>.request(listener: ResponseListener<T?>) {

    listener.onStart()
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, throwable: Throwable) {
            listener.onCompleted()
            onRequestCompleted(throwable)
        }

        override fun onResponse(call: Call<T>, response: Response<T?>) {
            listener.onCompleted()
            listener.onResponse(response)
        }
    })
}