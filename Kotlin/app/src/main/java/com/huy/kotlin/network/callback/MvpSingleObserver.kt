package com.huy.kotlin.network.callback

import com.huy.kotlin.base.mvp.BasePresenter
import com.huy.library.extension.toast
import io.reactivex.observers.DisposableSingleObserver
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/01/10
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class MvpSingleObserver<T>(private val presenter: BasePresenter<*>)
    : DisposableSingleObserver<T>() {


    /**
     * [DisposableSingleObserver] override
     */
    final override fun onStart() {
        presenter.view?.showProgress()
    }

    final override fun onSuccess(t: T) {
        hideProgress()
        onCompleted(t, null)
        onResponse(t)
    }

    final override fun onError(e: Throwable) {
        hideProgress()
        onCompleted(null, e)
        when (e) {
            is HttpException -> onFailed(e.code(), e.message())
            is SocketException, is UnknownHostException, is SocketTimeoutException -> onNetworkError()
            else -> onFailed(0, e.message ?: "")
        }
    }


    /**
     * Open callback wrapper
     */
    protected open fun onCompleted(data: T?, e: Throwable?) {
    }

    protected open fun onResponse(data: T) {}

    protected open fun onFailed(code: Int, message: String) {
        toast("$code $message")
    }

    protected open fun onNetworkError() {
        onFailed(0, "network error")
    }

    /**
     * Utils
     */
    private fun hideProgress() {
        if (!isDisposed) this.dispose()
        presenter.view?.hideProgress()
    }

}