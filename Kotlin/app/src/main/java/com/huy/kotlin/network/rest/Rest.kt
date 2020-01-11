package com.huy.kotlin.network.rest

import com.huy.kotlin.data.observable.EventLiveData
import com.huy.kotlin.data.observable.ProgressLiveData
import com.huy.kotlin.data.observable.ToastLiveData
import com.huy.library.extension.parse
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

const val UNKNOWN_ERROR = 998

const val NETWORK_ERROR = 999


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/07/21
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */


/**
 * Observable
 */
fun <T> Observable<T>.onNext(event: Int? = null, block: (Int, String, T?) -> Unit) {

    return subscribe(object : DisposableObserver<T?>() {
        override fun onStart() {
            onRequestStarted(event)
        }

        override fun onNext(response: T) {
            block(200, "OK", response)
        }

        override fun onComplete() {
            onRequestCompleted(event)
        }

        override fun onError(throwable: Throwable) {
            onRequestCompleted(throwable, block)
        }
    })
}

fun <T> Observable<T>.onSuccess(event: Int? = null, block: (T?) -> Unit): Disposable {

    return subscribeWith(object : DisposableObserver<T?>() {
        override fun onStart() {
            onRequestStarted(event)
        }

        override fun onNext(response: T) {
            block(response)
        }

        override fun onComplete() {
            onRequestCompleted(event)
        }

        override fun onError(throwable: Throwable) {
            onRequestCompleted(throwable)
        }
    })
}

fun <T> Observable<T>.liveData(event: Int? = null): EventLiveData<RestResponse<T>?> {

    return EventLiveData<RestResponse<T>?>().also { liveData ->
        onNext(event) { status, message, body ->
            liveData.postValue(RestResponse(status, message, body))
        }
    }
}

fun <T> Observable<T>.successLiveData(event: Int? = null): EventLiveData<T?> {

    return EventLiveData<T?>().also { liveData ->
        onSuccess(event) { response ->
            liveData.postValue(response)
        }
    }
}


/**
 * Single
 */
fun <T> Single<T>.onNext(event: Int? = null, block: (Int, String, T?) -> Unit) {

    subscribe(object : SingleObserver<T> {
        override fun onSubscribe(disposable: Disposable) {
            onRequestStarted(event)
        }

        override fun onSuccess(response: T) {
            onRequestCompleted(event)
            block(200, "OK", response)
        }

        override fun onError(throwable: Throwable) {
            onRequestCompleted(event)
            onRequestCompleted(throwable, block)
        }

    })
}

fun <T> Single<T>.onSuccess(event: Int? = null, block: (T) -> Unit) {

    subscribe(object : SingleObserver<T> {
        override fun onSubscribe(disposable: Disposable) {
            onRequestStarted(event)
        }

        override fun onSuccess(response: T) {
            onRequestCompleted(event)
            block(response)
        }

        override fun onError(throwable: Throwable) {
            onRequestCompleted(event)
        }
    })
}

fun <T> Single<T>.liveData(event: Int? = null): EventLiveData<RestResponse<T>?> {

    return EventLiveData<RestResponse<T>?>().also { liveData ->
        onNext(event) { status, message, body ->
            liveData.postValue(RestResponse(status, message, body))
        }
    }
}

fun <T> Single<T>.successLiveData(event: Int? = null): EventLiveData<T?> {

    return EventLiveData<T?>().also { liveData ->
        onSuccess(event) { response ->
            liveData.postValue(response)
        }
    }
}


/**
 * Flowable
 */
fun <T> Flowable<T>.onNext(event: Int? = null, block: (Int, String, T?) -> Unit) {

    subscribe(object : FlowableSubscriber<T> {
        override fun onSubscribe(subscription: Subscription) {
            onRequestStarted(event)
        }

        override fun onNext(response: T) {
            onRequestCompleted(event)
            block(200, "OK", response)
        }

        override fun onError(throwable: Throwable?) {
            onRequestCompleted(throwable, block)
        }

        override fun onComplete() {
            onRequestCompleted(event)
        }
    })
}

fun <T> Flowable<T>.onSuccess(event: Int? = null, block: (T?) -> Unit) {

    subscribe(object : FlowableSubscriber<T> {
        override fun onSubscribe(subscription: Subscription) {
            onRequestStarted(event)
        }

        override fun onNext(response: T) {
            onRequestCompleted(event)
            block(response)
        }

        override fun onError(throwable: Throwable?) {
            onRequestCompleted(throwable)
        }

        override fun onComplete() {
            onRequestCompleted(event)
        }
    })
}

fun <T> Flowable<T>.liveData(event: Int? = null): EventLiveData<RestResponse<T>?> {

    return EventLiveData<RestResponse<T>?>().also { liveData ->
        onNext(event) { status, message, body ->
            liveData.postValue(RestResponse(status, message, body))
        }
    }
}

fun <T> Flowable<T>.successLiveData(event: Int? = null): EventLiveData<T?> {

    return EventLiveData<T?>().also { liveData ->
        onSuccess(event) { response ->
            liveData.postValue(response)
        }
    }
}


/**
 * Call
 */
fun <T> Call<T>.onResponse(event: Int? = null, block: (Int, String, T?) -> Unit) {

    onRequestStarted(event)
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, throwable: Throwable) {
            onRequestCompleted(event)
            onRequestCompleted(throwable, block)
        }

        override fun onResponse(call: Call<T>, response: Response<T?>) {
            onRequestCompleted(event)
            onRequestSuccess(response) { status, message, body ->
                block(status, message, body)
            }
        }
    })
}

fun <T> Call<T>.onSuccess(event: Int? = null, block: (T?) -> Unit) {

    onRequestStarted(event)
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, throwable: Throwable) {
            onRequestCompleted(event)
            onRequestCompleted(throwable)
        }

        override fun onResponse(call: Call<T>, response: Response<T?>) {
            onRequestCompleted(event)
            onRequestSuccess(response) { _, _, body ->
                block(body)
            }
        }
    })
}

fun <T> Call<T>.liveData(event: Int? = null): EventLiveData<RestResponse<T>?> {

    return EventLiveData<RestResponse<T>?>().also { liveData ->
        onResponse(event) { status, message, body ->
            liveData.postValue(RestResponse(status, message, body))
        }
    }
}

fun <T> Call<T>.successLiveData(event: Int?): EventLiveData<T?> {
    return EventLiveData<T?>().also { liveData ->
        onSuccess(event) { response ->
            liveData.postValue(response)
        }
    }
}


/**
 * Callback handle
 */
fun <T> onRequestSuccess(response: Response<T?>?, block: (Int, String, T?) -> Unit) {

    if (response == null) {
        onRequestCompleted()
        return
    }

    if (response.isSuccessful) {
        block(response.code(), response.message(), response.body())
        return
    }

    if (response.errorBody() == null) {
        onRequestCompleted()
        return
    }

    try {
        val res = response.getErrorResponse() ?: return
        block(res.code, res.message, null)
        return
    } catch (e: java.lang.Exception) {
    }
}

fun <T> onRequestCompleted(throwable: Throwable? = null, block: (Int, String, T?) -> Unit) {
    when {
        throwable == null -> block(UNKNOWN_ERROR, "Some thing went wrong !", null)
        throwable is HttpException -> block(throwable.code(), throwable.message(), null)
        throwable.isNetworkError() -> block(NETWORK_ERROR, "network error", null)
        else -> block(-1, throwable.message ?: "Some thing went wrong !", null)
    }
}

fun onRequestCompleted(throwable: Throwable? = null) {
    ToastLiveData.message = when {
        throwable == null -> "Some thing went wrong !"
        throwable is HttpException -> "${throwable.code()} ${throwable.message()}"
        throwable.isNetworkError() -> "network error"
        else -> "Some thing went wrong !"
    }
}

fun onRequestStarted(event: Int?, hasProgress: Boolean = false) {
    RestClient.add(event)
    if (hasProgress) ProgressLiveData.show()
}

fun onRequestCompleted(event: Int?, hasProgress: Boolean = false) {
    RestClient.remove(event)
    if (hasProgress) ProgressLiveData.hide()
}


/**
 * Error handle
 */
private fun Response<*>.getErrorResponse(): RestResponse<*>? {

    return this.errorBody()?.string().parse(RestResponse::class.java)!!
}

private fun HttpException.getErrorMessage(): String {

    return try {
        return response()?.errorBody()?.toString() ?: ""
    } catch (e: Exception) {
        e.message ?: "some thing went wrong"
    }
}

fun Throwable.isNetworkError(): Boolean {
    return this is SocketException || this is UnknownHostException || this is SocketTimeoutException
}


