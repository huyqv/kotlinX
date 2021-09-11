package com.kotlin.app.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.app.data.network.errorBody
import com.sample.library.extension.stringyJson
import com.sample.library.util.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseVM : ViewModel() {

    val log by lazy { Logger(this::class.java.name) }

    fun <T> Flow<T>.launch(block: suspend T.() -> Unit) {
        onEach(block).launchIn(viewModelScope)
    }

    fun <T> Flow<Result<T?>>.onSuccess(block: T.() -> Unit): ResultHandler<T> {
        return ResultHandler(this).onSuccess(block)
    }

    fun <T> Flow<Result<T?>>.onFailure(block: Throwable.() -> Unit): ResultHandler<T> {
        return ResultHandler(this).onFailure(block)
    }

    inner class ResultHandler<T>(private val flow: Flow<Result<T?>>) {

        private var onSuccess: (T.() -> Unit)? = null
            set(value) {
                field = value
                launch()
            }

        private var onFailure: (Throwable.() -> Unit)? = null
            set(value) {
                field = value
                launch()
            }

        private var onNetworkException: Exception.() -> Unit = {
            onFailure?.invoke(this)
        }

        private var onHttpException: HttpException.() -> Unit = {
            onFailure?.invoke(this)
        }

        private var onCompleted: (() -> Unit)? = null

        private fun launch() {
            onSuccess ?: return
            onFailure ?: return
            flow.onEach {
                onCompleted?.invoke()
                if (it.isSuccess) {
                    onSuccess?.invoke(it.getOrNull()!!)
                } else {
                    onHandleThrowable(it.exceptionOrNull())
                }
            }.launchIn(viewModelScope)
        }

        private fun onHandleThrowable(it: Throwable?) {
            if (it == null) {
                onFailure?.invoke(NullPointerException("Unknown error"))
                return
            }
            val className = it::class.simpleName
            when (it) {
                is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                    log.d("$className: ${it.message}")
                    onNetworkException.invoke(this as Exception)
                }
                is HttpException -> {
                    log.d("$className: code ${it.code()}, message: ${it.message()}, body: ${it.errorBody.stringyJson()}")
                    onHttpException.invoke(it)
                }
                else -> {
                    log.d("$className: ${it.message}")
                    onFailure?.invoke(it)
                }
            }
        }

        fun onSuccess(block: T.() -> Unit): ResultHandler<T> {
            onSuccess = block
            return this@ResultHandler
        }

        fun onFailure(block: Throwable.() -> Unit): ResultHandler<T> {
            onFailure = block
            return this@ResultHandler
        }

        fun onNetworkException(block: Throwable.() -> Unit): ResultHandler<T> {
            onNetworkException = block
            return this@ResultHandler
        }

        fun onHttpException(block: Throwable.() -> Unit): ResultHandler<T> {
            onHttpException = block
            return this@ResultHandler
        }
    }

}