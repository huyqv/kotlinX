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


interface RestListener<T> {

    fun hasProgress(): Boolean {
        return true
    }

    fun onCompleted(code: Int, message: String, response: T?) {}

    fun onSuccessful(response: T) {}

    fun onFailed(code: Int, message: String) {}
}


/**
 * Observable
 */
fun <T> Observable<T>.onNext(tag: String? = null, progression: Boolean = false, block: (Int, String, T?) -> Unit): Disposable {

    return subscribeWith(object : DisposableObserver<T?>() {
        override fun onStart() {
            onRequestStarted(tag, progression)
        }

        override fun onNext(response: T) {
            block(200, "OK", response)
        }

        override fun onComplete() {
            onRequestCompleted(tag, progression)
        }

        override fun onError(throwable: Throwable) {
            onRequestCompleted(throwable, block)
        }
    })
}

fun <T> Observable<T>.onSuccess(tag: String? = null, progression: Boolean = false, block: (T?) -> Unit): Disposable {

    return subscribeWith(object : DisposableObserver<T?>() {
        override fun onStart() {
            onRequestStarted(tag, progression)
        }

        override fun onNext(response: T) {
            block(response)
        }

        override fun onComplete() {
            onRequestCompleted(tag, progression)
        }

        override fun onError(throwable: Throwable) {
            onRequestCompleted(throwable)
        }
    })
}

fun <T> Observable<T>.liveData(tag: String? = null, progression: Boolean = false): EventLiveData<RestResponse<T>?> {

    return EventLiveData<RestResponse<T>?>().also { liveData ->
        onNext(tag, progression) { status, message, body ->
            liveData.postValue(RestResponse(status, message, body))
        }
    }
}

fun <T> Observable<T>.successLiveData(tag: String? = null, progression: Boolean = false): EventLiveData<T?> {

    return EventLiveData<T?>().also { liveData ->
        onSuccess(tag, progression) { response ->
            liveData.postValue(response)
        }
    }
}


/**
 * Single
 */
fun <T> Single<T>.onNext(tag: String? = null, progression: Boolean = false, block: (Int, String, T?) -> Unit) {

    subscribe(object : SingleObserver<T> {
        override fun onSubscribe(disposable: Disposable) {
            onRequestStarted(tag, progression)
        }

        override fun onSuccess(response: T) {
            onRequestCompleted(tag, progression)
            block(200, "OK", response)
        }

        override fun onError(throwable: Throwable) {
            onRequestCompleted(tag, progression)
            onRequestCompleted(throwable, block)
        }
    })
}

fun <T> Single<T>.onSuccess(tag: String? = null, progression: Boolean = false, block: (T) -> Unit) {

    subscribe(object : SingleObserver<T> {
        override fun onSubscribe(disposable: Disposable) {
            onRequestStarted(tag, progression)
        }

        override fun onSuccess(response: T) {
            onRequestCompleted(tag, progression)
            block(response)
        }

        override fun onError(throwable: Throwable) {
            onRequestCompleted(tag, progression)
        }
    })
}

fun <T> Single<T>.liveData(tag: String? = null, progression: Boolean = false): EventLiveData<RestResponse<T>?> {

    return EventLiveData<RestResponse<T>?>().also { liveData ->
        onNext(tag, progression) { status, message, body ->
            liveData.postValue(RestResponse(status, message, body))
        }
    }
}

fun <T> Single<T>.successLiveData(tag: String? = null, progression: Boolean = false): EventLiveData<T?> {

    return EventLiveData<T?>().also { liveData ->
        onSuccess(tag, progression) { response ->
            liveData.postValue(response)
        }
    }
}


/**
 * Flowable
 */
fun <T> Flowable<T>.onNext(tag: String? = null, progression: Boolean = false, block: (Int, String, T?) -> Unit) {

    subscribe(object : FlowableSubscriber<T> {
        override fun onSubscribe(subscription: Subscription) {
            onRequestStarted(tag, progression)
        }

        override fun onNext(response: T) {
            onRequestCompleted(tag, progression)
            block(200, "OK", response)
        }

        override fun onError(throwable: Throwable?) {
            onRequestCompleted(throwable, block)
        }

        override fun onComplete() {
            onRequestCompleted(tag, progression)
        }
    })
}

fun <T> Flowable<T>.onSuccess(tag: String? = null, progression: Boolean = false, block: (T?) -> Unit) {

    subscribe(object : FlowableSubscriber<T> {
        override fun onSubscribe(subscription: Subscription) {
            onRequestStarted(tag, progression)
        }

        override fun onNext(response: T) {
            onRequestCompleted(tag, progression)
            block(response)
        }

        override fun onError(throwable: Throwable?) {
            onRequestCompleted(throwable)
        }

        override fun onComplete() {
            onRequestCompleted(tag, progression)
        }
    })
}

fun <T> Flowable<T>.liveData(tag: String? = null, progression: Boolean = false): EventLiveData<RestResponse<T>?> {

    return EventLiveData<RestResponse<T>?>().also { liveData ->
        onNext(tag, progression) { status, message, body ->
            liveData.postValue(RestResponse(status, message, body))
        }
    }
}

fun <T> Flowable<T>.successLiveData(tag: String? = null, progression: Boolean = false): EventLiveData<T?> {

    return EventLiveData<T?>().also { liveData ->
        onSuccess(tag, progression) { response ->
            liveData.postValue(response)
        }
    }
}


/**
 * Call
 */
fun <T> Call<T>.onResponse(tag: String? = null, progression: Boolean = false, block: (Int, String, T?) -> Unit) {

    onRequestStarted(tag, progression)
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, throwable: Throwable) {
            onRequestCompleted(tag, progression)
            onRequestCompleted(throwable, block)
        }

        override fun onResponse(call: Call<T>, response: Response<T?>) {
            onRequestCompleted(tag, progression)
            onRequestSuccess(response) { status, message, body ->
                block(status, message, body)
            }
        }
    })
}

fun <T> Call<T>.onSuccess(tag: String? = null, progression: Boolean = false, block: (T?) -> Unit) {

    onRequestStarted(tag, progression)
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, throwable: Throwable) {
            onRequestCompleted(tag, progression)
            onRequestCompleted(throwable)
        }

        override fun onResponse(call: Call<T>, response: Response<T?>) {
            onRequestCompleted(tag, progression)
            onRequestSuccess(response) { _, _, body ->
                block(body)
            }
        }
    })
}

fun <T> Call<T>.liveData(tag: String? = null, progression: Boolean = false): EventLiveData<RestResponse<T>?> {

    return EventLiveData<RestResponse<T>?>().also { liveData ->
        onResponse(tag, progression) { status, message, body ->
            liveData.postValue(RestResponse(status, message, body))
        }
    }
}

fun <T> Call<T>.successLiveData(tag: String? = null, progression: Boolean = false): EventLiveData<T?> {
    return EventLiveData<T?>().also { liveData ->
        onSuccess(tag, progression) { response ->
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
        block(res.status, res.message, null)
        return
    } catch (e: java.lang.Exception) {
    }
}

fun <T> onRequestCompleted(throwable: Throwable? = null, block: (Int, String, T?) -> Unit) {
    when {
        throwable == null -> block(UNKNOWN_ERROR, "Some thing went wrong !", null)
        throwable is HttpException -> block(throwable.code(), throwable.message(), null)
        throwable.isNetworkError() -> block(NETWORK_ERROR, "network error", null)
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

fun onRequestStarted(tag: String?, progression: Boolean) {
    RestClient.add(tag)
    ProgressLiveData.show(progression)
}

fun onRequestCompleted(tag: String?, progression: Boolean) {
    RestClient.remove(tag)
    ProgressLiveData.hide(progression)
}


/**
 * Error handle
 */
private fun Response<*>.getErrorResponse(): RestResponse<*>? {

    return this.errorBody()!!.string().parse(RestResponse::class.java)!!
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


