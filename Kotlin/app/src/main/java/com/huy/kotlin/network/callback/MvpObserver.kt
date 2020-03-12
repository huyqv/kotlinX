package com.huy.kotlin.network.callback

import com.huy.kotlin.base.mvp.BasePresenter
import com.huy.library.extension.toast
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
abstract class MvpObserver<T>(private val presenter: BasePresenter<*>)
    : DisposableObserver<T>() {


    /**
     * [DisposableObserver] implement
     */
    final override fun onStart() {
        presenter.view?.showProgress()
    }

    final override fun onComplete() {
        if (!isDisposed) this.dispose()
        presenter.view?.hideProgress()
    }

    final override fun onNext(t: T) {
        onCompleted(t, null)
    }

    final override fun onError(e: Throwable) {
        onCompleted(null, e)
    }


    /**
     * Open callback wrapper methods
     */
    protected open fun onCompleted(body: T?, e: Throwable?) {
        if (null != body) {
            onResponse(body)
        } else when (e) {
            is HttpException -> onFailed(e.code(), e.message())
            is SocketException, is UnknownHostException, is SocketTimeoutException -> onNetworkError()
            else -> onFailed(0, e?.message ?: "")
        }
    }

    protected open fun onResponse(body: T) {}

    protected open fun onFailed(code: Int, message: String) {
        toast("$code $message")
    }

    protected open fun onNetworkError() {
        toast("network error")
    }

}