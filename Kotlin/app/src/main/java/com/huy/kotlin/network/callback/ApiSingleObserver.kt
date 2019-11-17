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

    private val progression: Boolean

    private val block: (T?) -> Unit

    constructor(tag: String? = null, progression: Boolean = false, block: (T?) -> Unit) {
        this.tag = tag
        this.progression = progression
        this.block = block
    }

    override fun onSubscribe(disposable: Disposable) {
        onRequestStarted(tag, progression)
    }

    override fun onSuccess(response: T) {
        onRequestCompleted(tag, progression)
        block(response)
    }

    override fun onError(throwable: Throwable) {
        onRequestCompleted(tag, progression)
        onRequestCompleted(throwable)
    }

}
