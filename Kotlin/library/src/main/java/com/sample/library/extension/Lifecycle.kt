package com.sample.library.extension

import android.app.Activity
import android.view.Window
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.KClass

fun LifecycleOwner.requireActivity(): Activity? {
    return (this as? Fragment)?.requireActivity() ?: (this as? Activity)
}

fun LifecycleOwner.requireWindow(): Window? {
    return requireActivity()?.window
}

fun <T : ViewModel> ViewModelStoreOwner.viewModel(cls: KClass<T>): T {
    return ViewModelProvider(this).get(cls.java)
}

fun <T : ViewModel> ViewModelStoreOwner.lazyViewModel(cls: KClass<T>): Lazy<T> {
    return lazy { viewModel(cls) }
}

fun <T : ViewModel> Fragment.activityVM(cls: KClass<T>): T {
    return ViewModelProvider(requireActivity()).get(cls.java)
}

fun <T : ViewModel> Fragment.lazyActivityVM(cls: KClass<T>): Lazy<T> {
    return lazy { ViewModelProvider(requireActivity()).get(cls.java) }
}

fun <T : ViewModel> Fragment.newActivityVM(cls: KClass<T>): T {
    return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[cls.java]
}

fun <T> LiveData<T?>.nonNull(): NonNullLiveData<T> {
    val mediator: NonNullLiveData<T> = NonNullLiveData()
    mediator.addSource(this) { data ->
        data?.let {
            mediator.value = data
        }
    }
    return mediator
}

fun <R, T : LiveData<R>> T.event(): T {
    val result = SingleLiveData<R>()
    result.addSource(this) {
        @Suppress("UNCHECKED_CAST")
        result.value = it as R
    }
    @Suppress("UNCHECKED_CAST")
    return result as T
}

fun <R, T : LiveData<R>> T.noneNull(): T {
    val result = NonNullLiveData<R>()
    result.addSource(this) {
        @Suppress("UNCHECKED_CAST")
        result.value = it as R
    }
    @Suppress("UNCHECKED_CAST")
    return result as T
}

inline fun <T> LiveData<T?>.observe(owner: LifecycleOwner, crossinline block: (t: T?) -> Unit) {
    this.observe(owner, Observer {
        block(it)
    })
}

fun <T> NonNullLiveData<T>.observe(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, Observer {
        it?.let(observer)
    })
}

/**
 * Live data only trigger when data change for multi observer
 */
open class SingleLiveData<T> : MediatorLiveData<T>() {

    private val observers = ConcurrentHashMap<LifecycleOwner, MutableSet<ObserverWrapper<T>>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        val set = observers[owner]
        set?.apply {
            @Suppress("UNCHECKED_CAST")
            add(wrapper as ObserverWrapper<T>)
        } ?: run {
            val newSet = Collections.newSetFromMap(ConcurrentHashMap<ObserverWrapper<T>, Boolean>())
            @Suppress("UNCHECKED_CAST")
            newSet.add(wrapper as ObserverWrapper<T>?)
            observers[owner] = newSet
        }
        super.observe(owner, wrapper)
    }

    override fun removeObservers(owner: LifecycleOwner) {
        observers.remove(owner)
        super.removeObservers(owner)
    }

    override fun removeObserver(observer: Observer<in T>) {
        observers.forEach {
            if (it.value.remove(observer as Observer<*>)) {
                if (it.value.isEmpty()) {
                    observers.remove(it.key)
                }
                return@forEach
            }
        }
        super.removeObserver(observer)
    }

    @MainThread
    override fun setValue(t: T?) {
        observers.forEach { it.value.forEach { wrapper -> wrapper.newValue() } }
        super.setValue(t)
    }

    protected open fun onDataChanged(t: T?) {
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

    private inner class ObserverWrapper<R>(private val observer: Observer<R>) : Observer<R> {

        private val pending = AtomicBoolean(false)

        override fun onChanged(t: R?) {
            if (pending.compareAndSet(true, false)) {
                @Suppress("UNCHECKED_CAST")
                (t as? T)?.also { onDataChanged(it) }
                observer.onChanged(t)
            }
        }

        fun newValue() {
            pending.set(true)
        }
    }
}

open class EventLiveData : MediatorLiveData<Boolean?>() {

    private val observers = ConcurrentHashMap<LifecycleOwner, MutableSet<ObserverWrapper>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in Boolean?>) {
        val wrapper = ObserverWrapper(observer)
        val set = observers[owner]
        set?.apply {
            @Suppress("UNCHECKED_CAST")
            add(wrapper)
        } ?: run {
            val newSet = Collections.newSetFromMap(ConcurrentHashMap<ObserverWrapper, Boolean>())
            @Suppress("UNCHECKED_CAST")
            newSet.add(wrapper as ObserverWrapper?)
            observers[owner] = newSet
        }
        super.observe(owner, wrapper)
    }

    override fun removeObservers(owner: LifecycleOwner) {
        observers.remove(owner)
        super.removeObservers(owner)
    }

    override fun removeObserver(observer: Observer<in Boolean?>) {
        observers.forEach {
            if (it.value.remove(observer as EventLiveData.ObserverWrapper)) {
                if (it.value.isEmpty()) {
                    observers.remove(it.key)
                }
                return@forEach
            }
        }
        super.removeObserver(observer)
    }

    @MainThread
    override fun setValue(t: Boolean?) {
        observers.forEach { it.value.forEach { wrapper -> wrapper.newValue() } }
        super.setValue(t)
    }

    protected open fun onDataChanged(t: Boolean?) {
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

    private inner class ObserverWrapper(private val observer: Observer<in Boolean?>) :
        Observer<Boolean?> {

        private val pending = AtomicBoolean(false)

        override fun onChanged(t: Boolean?) {
            if (pending.compareAndSet(true, false)) {
                when (t) {
                    true -> {
                        onDataChanged(t)
                        observer.onChanged(t)
                    }
                }
            }
        }

        fun newValue() {
            pending.set(true)
        }
    }
}

/**
 * Live data only trigger when data change if value none null
 */
class NonNullLiveData<T> : MediatorLiveData<T>()

abstract class SimpleLifecycleObserver : LifecycleObserver {

    open fun onCreated() = Unit
    open fun onStart() = Unit
    open fun onResume() = Unit
    open fun onPause() = Unit
    open fun onStop() = Unit
    open fun onDestroy() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onEventCreated() {
        onCreated()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEventStart() {
        onStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onEventResume() {
        onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onEventPause() {
        onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEventStop() {
        onStop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onEventDestroy() {
        onDestroy()
    }
}


