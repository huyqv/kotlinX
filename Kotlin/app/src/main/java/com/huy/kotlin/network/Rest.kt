package com.huy.kotlin.network

import com.google.gson.JsonObject
import com.huy.kotlin.base.mvp.BasePresenter
import com.huy.kotlin.network.callback.ArchObserver
import com.huy.kotlin.network.callback.ArchSingleObserver
import com.huy.kotlin.network.callback.MvpObserver
import com.huy.kotlin.network.callback.MvpSingleObserver
import com.huy.library.extension.parse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.HttpException
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
private fun HttpException.errorBody(): JsonObject? {
    return response()?.errorBody()?.toString().parse(JsonObject::class.java)
}

fun Throwable.isNetworkError(): Boolean {
    return this is SocketException || this is UnknownHostException || this is SocketTimeoutException
}

fun <T> Observable<T>.request() {
    subscribe(object : ArchObserver<T>() {})
}

fun <T> Single<T>.request() {
    subscribe(object : ArchSingleObserver<T>() {})
}

fun <T> Observable<T>.request(presenter: BasePresenter<*>) {
    subscribe(object : MvpObserver<T>(presenter) {})
}

fun <T> Single<T>.request(presenter: BasePresenter<*>) {
    subscribe(object : MvpSingleObserver<T>(presenter) {})
}


