package com.kotlin.app.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.library.util.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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

        fun launch() {
            if (onSuccess != null && onFailure != null) {
                flow.onEach {
                    if (it.isSuccess) {
                        onSuccess?.invoke(it.getOrNull()!!)
                    } else {
                        onFailure?.invoke(it.exceptionOrNull()!!)
                    }
                }.launchIn(viewModelScope)
            }
        }

        fun onSuccess(block: T.() -> Unit): ResultHandler<T> {
            onSuccess = block
            launch()
            return this@ResultHandler
        }

        fun onFailure(block: Throwable.() -> Unit): ResultHandler<T> {
            onFailure = block
            launch()
            return this@ResultHandler
        }
    }

}