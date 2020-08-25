package com.huy.kotlin.util

import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.huy.kotlin.data.observable.SingleLiveData

val PAGED_DEFAULT_CONFIG: PagedList.Config
    get() = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .setEnablePlaceholders(true)
            .build()

class NonNullLiveData<T> : MediatorLiveData<T>()

/**
 * Use: myLiveData.nonNull().observer(..)
 */
fun <T> LiveData<T?>.nonNull(): NonNullLiveData<T> {
    val mediator: NonNullLiveData<T> = NonNullLiveData()
    mediator.addSource(this) { data ->
        data?.let {
            mediator.value = data
        }
    }
    return mediator
}

fun doSomeThing1(block: TextView.() -> Int) {
}

fun doSomeThing2(block: (TextView) -> Int) {
}

/**
 * Use: myLiveData.single().observer(..)
 */
fun <T> LiveData<T>.single(): LiveData<T> {
    val result = SingleLiveData<T>()
    result.addSource(this) {
        result.value = it
    }
    return result
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
