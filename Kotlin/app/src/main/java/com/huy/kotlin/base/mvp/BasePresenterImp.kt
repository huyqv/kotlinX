package com.huy.kotlin.base.mvp

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import com.huy.kotlin.RoomHelper
import com.huy.kotlin.app.App
import com.huy.kotlin.base.event.Event
import com.huy.kotlin.base.event.EventDispatcher
import com.huy.kotlin.base.event.EventListener
import com.huy.kotlin.base.view.BaseView
import com.huy.kotlin.data.local.SharedHelper
import com.huy.kotlin.network.callback.WeakDisposableObserver
import com.huy.kotlin.network.rest.RestClient
import com.huy.kotlin.network.rest.RestResponse
import com.huy.library.extension.toast
import com.huy.library.util.NetworkUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.lang.ref.WeakReference

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
open class BasePresenterImp<V : BaseView> : BasePresenter<V>, EventListener {

    private var composite: CompositeDisposable? = null

    private var viewRef: WeakReference<V>? = null

    protected val view: V? get() = viewRef?.get()

    override fun attach(view: V) {
        if (viewAttached()) return
        viewRef = WeakReference(view)
    }

    override fun detach() {
        viewRef?.clear()
        viewRef = null
        composite?.dispose()
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

    val context: Context get() = App.appContext

    val room: RoomHelper get() = RoomHelper.instance

    val shared: SharedHelper get() = SharedHelper.instance

    val service: RestClient get() = RestClient.instance

    fun getString(@StringRes res: Int): String {
        return context.resources.getString(res)
    }

    fun getComposite(): CompositeDisposable {

        if (null == composite || composite?.isDisposed == true)
            composite = CompositeDisposable()

        return composite!!
    }

    fun <T> requestApi(observable: Observable<RestResponse<T>>, onCompleted: (RestResponse<T>) -> Unit) {

        if (viewDetached()) return

        @Suppress("UNCHECKED_CAST")
        val observer = object : WeakDisposableObserver<RestResponse<T>>(viewRef as WeakReference<BaseView>) {
            override fun onSuccess(response: RestResponse<T>) {
                onCompleted(response)
            }

            override fun onComplete() {
                super.onComplete()
                getComposite().remove(this)
            }
        }

        val disposable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        getComposite().add(disposable)
    }

    fun <T> request(observable: Observable<RestResponse<T>>, onCompleted: (RestResponse<T>) -> Unit) {

        if (viewDetached()) return

        val observer = object : DisposableObserver<RestResponse<T>>() {

            override fun onNext(t: RestResponse<T>) {
                onCompleted(t)
                view?.hideProgress()
            }

            override fun onError(throwable: Throwable) {
                view?.hideProgress()
                when {
                    throwable is HttpException -> toast(throwable.message())
                    NetworkUtil.isNetworkException(throwable) -> view?.networkError()
                    else -> view?.unknownError()
                }
            }

            override fun onComplete() {
                view?.hideProgress()
                getComposite().remove(this)
            }
        }

        val disposable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        getComposite().add(disposable)
    }

}