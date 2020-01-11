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

    private val event: Int?

    private val block: (T?) -> Unit

    constructor(event: Int? = null, block: (T?) -> Unit) {
        this.event = event
        this.block = block
    }

    open fun hasProgress(): Boolean = true

    override fun onSubscribe(disposable: Disposable) {
        onRequestStarted(event, hasProgress())
    }

    override fun onSuccess(response: T) {
        onRequestCompleted(event, hasProgress())
        block(response)
    }

    override fun onError(throwable: Throwable) {
        onRequestCompleted(event, hasProgress())
        onRequestCompleted(throwable)
    }

}
