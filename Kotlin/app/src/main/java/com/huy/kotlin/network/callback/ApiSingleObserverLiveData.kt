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
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ApiSingleObserverLiveData<T> : MutableLiveData<T>, SingleObserver<T> {

    private val tag: String?

    private val progression: Boolean

    constructor(tag: String? = null, progression: Boolean = false) {
        this.tag = tag
        this.progression = progression
    }

    override fun onSubscribe(disposable: Disposable) {
        onRequestStarted(tag, progression)
    }

    override fun onSuccess(response: T) {
        onRequestCompleted(tag, progression)
        postValue(response)
    }

    override fun onError(throwable: Throwable) {
        onRequestCompleted(tag, progression)
        onRequestCompleted(throwable)
    }

}