package sample.util

import androidx.lifecycle.*
import com.sample.library.fragment.BasePagerInfinityAdapter
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

abstract class IntervalObservable<T> {

    var delayInterval: Long = 0

    private var disposable: Disposable? = null

    abstract fun onData(): T

    open fun onComplete() {}

    abstract fun onNext(t: T)

    abstract fun onError(e: Throwable)

    fun start() {
        disposable?.dispose()
        disposable = Observable
                .interval(0, 1000, TimeUnit.MILLISECONDS)
                .map { onData() }
                .subscribeOn(Schedulers.io())
                .delay(delayInterval, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<T>() {
                    override fun onComplete() {
                        this@IntervalObservable.onComplete()
                    }

                    override fun onNext(t: T) {
                        this@IntervalObservable.onNext(t)
                    }

                    override fun onError(e: Throwable) {
                        this@IntervalObservable.onError(e)
                    }
                })
    }

    fun stop() {
        disposable?.dispose()
    }
}

abstract class IntervalSingle<T> {

    var delayInterval: Long = 0

    private var disposable: Disposable? = null

    abstract fun onData(): T

    open fun onSubscribe(d: Disposable) {}

    abstract fun onSuccess(t: T)

    abstract fun onError(e: Throwable)

    fun start() {
        disposable?.dispose()
        disposable = Single
                .just(onData())
                .subscribeOn(Schedulers.io())
                .delay(delayInterval, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent { t, e ->
                    if (t != null) {
                        onSuccess(t)
                    } else if (e != null) {
                        onError(e)
                    }
                    start()
                }
                .subscribe()
    }

    fun stop() {
        disposable?.dispose()
    }

}

abstract class AutoSlideAdapter<T> : BasePagerInfinityAdapter<Int>() {

    val pageLiveData = MutableLiveData<Int>()

    val atomicInteger = AtomicInteger()

    val intervalSingle = object : IntervalSingle<Int>() {
        override fun onData(): Int {
            if (atomicInteger.incrementAndGet() > data.lastIndex) atomicInteger.set(0)
            return atomicInteger.get()
        }

        override fun onSuccess(t: Int) {
            pageLiveData.postValue(t)
        }

        override fun onError(e: Throwable) {
        }
    }

    fun observer(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                intervalSingle.start()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                intervalSingle.stop()
            }
        })
    }

}

