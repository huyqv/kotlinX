package com.huy.kotlin.network.callback

import com.huy.kotlin.network.rest.onRequestCompleted
import com.huy.kotlin.network.rest.onRequestStarted
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/01/10
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ApiSingleObserver<T> : SingleObserver<T> {

    private val tag: String?

    private val block: (T?) -> Unit

    constructor(tag: String? = null, block: (T?) -> Unit) {
        this.tag = tag
        this.block = block
    }

    open fun hasProgress(): Boolean = true

    override fun onSubscribe(disposable: Disposable) {
        onRequestStarted(tag, hasProgress())
    }

    override fun onSuccess(response: T) {
        onRequestCompleted(tag, hasProgress())
        block(response)
    }

    override fun onError(throwable: Throwable) {
        onRequestCompleted(tag, hasProgress())
        onRequestCompleted(throwable)
    }

}
