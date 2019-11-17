package com.huy.kotlin.network.callback

import androidx.lifecycle.MutableLiveData
import com.huy.kotlin.data.observable.ProgressLiveData
import com.huy.kotlin.network.rest.onRequestCompleted
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableHelper
import io.reactivex.internal.util.EndConsumerHelper
import java.util.concurrent.atomic.AtomicReference

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/01/10
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
open class ApiDisposableObserverLiveData<T> : MutableLiveData<T>, Observer<T>, Disposable {

    private val upstream = AtomicReference<Disposable>()

    private val progression: Boolean

    constructor(progression: Boolean = false) {
        this.progression = progression
    }

    override fun isDisposed(): Boolean {
        return upstream.get() === DisposableHelper.DISPOSED
    }

    override fun dispose() {
        DisposableHelper.dispose(upstream)
    }

    /**
     * Called once the single upstream Disposable is submit via onSubscribe.
     */
    override fun onSubscribe(disposable: Disposable) {
        if (EndConsumerHelper.setOnce(this.upstream, disposable, javaClass)) {
            onStart()
        }
    }

    override fun onNext(response: T) {
        postValue(response)
    }

    override fun onError(throwable: Throwable) {
        onRequestCompleted(throwable)
    }

    override fun onComplete() {
        ProgressLiveData.hide(progression)
    }

    protected fun onStart() {
        ProgressLiveData.show(progression)
    }

}