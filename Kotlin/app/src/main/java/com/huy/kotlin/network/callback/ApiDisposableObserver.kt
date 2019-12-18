package com.huy.kotlin.network.callback

import com.huy.kotlin.data.observable.ProgressLiveData
import com.huy.kotlin.network.rest.onRequestCompleted
import io.reactivex.observers.DisposableObserver

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/07/20
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class ApiDisposableObserver<T> : DisposableObserver<T?> {

    private val block: (T?) -> Unit

    constructor(block: (T?) -> Unit) {
        this.block = block
    }

    open fun hasProgress(): Boolean = true

    override fun onNext(t: T) {
        block(t)
    }

    override fun onError(throwable: Throwable) {
        onRequestCompleted(throwable)
    }

    override fun onComplete() {
        ProgressLiveData.hide()
    }

    override fun onStart() {
        ProgressLiveData.show()
    }
}