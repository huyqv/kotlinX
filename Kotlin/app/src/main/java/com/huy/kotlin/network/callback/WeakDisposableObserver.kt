package com.huy.kotlin.network.callback

import com.huy.kotlin.base.view.BaseView
import com.huy.library.extension.toast
import io.reactivex.observers.DisposableObserver
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.ref.WeakReference
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
abstract class WeakDisposableObserver<T> constructor(private val viewRef: WeakReference<BaseView>) : DisposableObserver<T>() {

    protected abstract fun onSuccess(response: T)

    override fun onNext(response: T) {
        viewRef.get()?.run {
            onSuccess(response)
            hideProgress()
        }
    }

    override fun onError(throwable: Throwable) {
        viewRef.get()?.run {
            hideProgress()
            when (throwable) {
                is HttpException -> toast("${throwable.code()} ${throwable.getErrorMessage()}")
                is SocketException, is UnknownHostException, is SocketTimeoutException -> networkError()
                else -> unknownError()
            }
        }
    }

    override fun onComplete() {
        viewRef.get()?.hideProgress()
    }

    private fun HttpException.getErrorMessage(): String {

        return try {
            val responseBody = this.response()?.errorBody()
            val jsonObject = JSONObject(responseBody!!.string())
            jsonObject.getString("message")
        } catch (exception: Exception) {
            exception.message ?: "some thing went wrong"
        }
    }

}