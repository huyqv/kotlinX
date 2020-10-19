package com.huy.kotlin.base.mvp

import com.huy.kotlin.base.view.BaseView
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
open class BasePresenterImp<V : BaseView> : BasePresenter<V> {

    private val composite: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private var viewRef: WeakReference<V>? = null

    /**
     * [BasePresenter] implement
     */
    override val view: V? get() = viewRef?.get()

    override fun attach(view: V) {
        if (viewAttached) return
        viewRef = WeakReference(view)
    }

    override fun detach() {
        viewRef?.clear()
        viewRef = null
        composite.dispose()
    }

}