package com.huy.kotlin.network.callback

import androidx.lifecycle.MutableLiveData
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
class ApiSingleObserverLiveData<T> : MutableLiveData<T>, SingleObserver<T> {

    private val tag: String?

    constructor(tag: String? = null) {
        this.tag = tag
    }

    open fun hasProgress(): Boolean = true

    override fun onSubscribe(disposable: Disposable) {
        onRequestStarted(tag, hasProgress())
    }

    override fun onSuccess(response: T) {
        onRequestCompleted(tag, hasProgress())
        postValue(response)
    }

    override fun onError(throwable: Throwable) {
        onRequestCompleted(tag, hasProgress())
        onRequestCompleted(throwable)
    }

}