package com.kotlin.app.data.api

import android.os.Build
import android.os.Environment
import com.kotlin.app.app.App
import com.kotlin.app.data.api.observable.ApiObserver
import com.kotlin.app.data.api.observable.ApiSingleObserver
import com.example.library.extension.parse
import com.google.gson.JsonObject
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
    subscribe(object : ApiObserver<T>() {})
}

fun <T> Single<T>.request() {
    subscribe(object : ApiSingleObserver<T>() {})
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



