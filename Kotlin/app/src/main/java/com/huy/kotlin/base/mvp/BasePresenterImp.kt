package com.huy.kotlin.base.mvp

import android.content.Context
import android.util.Log
import com.huy.kotlin.app.App
import com.huy.kotlin.base.event.Event
import com.huy.kotlin.base.event.EventDispatcher
import com.huy.kotlin.base.event.EventListener
import com.huy.kotlin.base.view.BaseView
import com.huy.kotlin.data.RoomDB
import com.huy.kotlin.data.Shared
import com.huy.kotlin.data.api.ApiClient
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
open class BasePresenterImp<V : BaseView> : BasePresenter<V>, EventListener {

    protected val context: Context get() = App.instance.applicationContext

    protected val room: RoomDB get() = RoomDB.instance

    protected val shared: Shared get() = Shared.instance

    protected val service: ApiClient get() = ApiClient.instance

    private val composite: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    /**
     * [BasePresenter] implement
     */
    override var viewRef: WeakReference<V>? = null

    override val view: V? get() = viewRef?.get()

    override fun attach(view: V) {
        if (viewAttached()) return
        viewRef = WeakReference(view)
    }

    override fun detach() {
        viewRef?.clear()
        viewRef = null
        composite.dispose()
    }

    override fun viewAttached(): Boolean {
        return null != view
    }

    override fun viewDetached(): Boolean {
        return null == view
    }

    override fun onStart() {
        EventDispatcher.instance.addListener(Event.CANCEL_REQUEST, listener = this)
    }

    override fun onStop() {
        EventDispatcher.instance.removeListener(Event.CANCEL_REQUEST, listener = this)
    }

    override fun onEvent(id: Int, vararg args: Any?) {
        Log.d("EventPost", id.toString())
    }

}