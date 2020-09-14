package com.huy.kotlin.data.api

import android.os.Build
import android.os.Environment
import com.google.gson.JsonObject
import com.huy.kotlin.App
import com.huy.kotlin.base.mvp.BasePresenter
import com.huy.kotlin.data.observable.ArchObserver
import com.huy.kotlin.data.observable.ArchSingleObserver
import com.huy.kotlin.data.observable.MvpObserver
import com.huy.kotlin.data.observable.MvpSingleObserver
import com.huy.library.extension.parse
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import okio.buffer
import okio.sink
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/07/21
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
private fun HttpException.errorBody(): JsonObject? {
    return response()?.errorBody()?.toString().parse(JsonObject::class.java)
}

fun Throwable.isNetworkError(): Boolean {
    return this is SocketException || this is UnknownHostException || this is SocketTimeoutException
}

fun <T> Observable<T>.request() {
    subscribe(object : ArchObserver<T>() {})
}

fun <T> Single<T>.request() {
    subscribe(object : ArchSingleObserver<T>() {})
}

fun <T> Observable<T>.request(presenter: BasePresenter<*>) {
    subscribe(object : MvpObserver<T>(presenter) {})
}

fun <T> Single<T>.request(presenter: BasePresenter<*>) {
    subscribe(object : MvpSingleObserver<T>(presenter) {})
}

fun Observable<Response<ResponseBody>>.writeFile(fileName: String): Observable<File> {
    return flatMap(object : Function<Response<ResponseBody>, ObservableSource<File>> {
        override fun apply(response: Response<ResponseBody>): ObservableSource<File> {
            return Observable.create(object : ObservableOnSubscribe<File> {
                override fun subscribe(emitter: ObservableEmitter<File>) {
                    val source = response.body()?.source()
                            ?: throw NullPointerException("download data is empty")
                    try {
                        val file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            File(App.instance.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath, fileName)
                        } else {
                            @Suppress("DEPRECATION")
                            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
                        }
                        file.sink().buffer().apply {
                            writeAll(source)
                            close()
                        }
                        emitter.onNext(file)
                    } catch (e: IOException) {
                        emitter.onError(e)
                    } finally {
                        emitter.onComplete()
                    }
                }
            })
        }
    })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

