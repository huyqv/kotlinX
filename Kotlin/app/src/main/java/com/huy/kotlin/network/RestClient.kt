package com.huy.kotlin.network

import com.huy.kotlin.BuildConfig
import com.huy.kotlin.ui.model.Image
import com.huy.kotlin.ui.model.Message
import com.huy.kotlin.ui.model.User
import com.huy.library.extension.downloadFile
import com.huy.library.extension.parse
import io.reactivex.*
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import okio.buffer
import okio.sink
import retrofit2.Response
import java.io.File
import java.io.IOException

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/1/2
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RestClient private constructor() {

    var service: RestService = RestHelper.createService(BuildConfig.SERVICE_URL)
            .build()
            .create(RestService::class.java)


    private var requests: MutableList<Single<*>> = mutableListOf()

    private fun <T> Single<T>.filterRequest(): Single<T> {
        return doOnSubscribe {
            requests.add(this)
        }.doOnDispose {
            requests.remove(this)
        }.doOnEvent { _, _ ->
            requests.remove(this)
        }
    }

    companion object {

        val instance: RestClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            RestClient()
        }

        fun setToken(token: String? = null) {
            instance.service = RestHelper
                    .createService(BuildConfig.SERVICE_URL) {
                        if (token != null) {
                            addInterceptor(RestHelper.getHeaderInterceptor(token))
                        }
                    }
                    .build()
                    .create(RestService::class.java)
        }

    }

    fun messages(page: Int): Single<List<Message>?> {
        return service.arrayGET("api/messages$page")
                .filterRequest()
                .map {
                    it.parse(Array<Message>::class.java)
                }
    }

    fun images(page: Int): Single<List<Image>> {
        return service.arrayGET("api/images$page")
                .filterRequest()
                .map {
                    it.parse(Array<Image>::class.java)
                }
    }

    fun users(page: Int): Single<List<User>> {
        return service.arrayGET("api/users$page")
                .filterRequest()
                .map {
                    it.parse(Array<User>::class.java)
                }
    }

    fun download(fileName: String, url: String): Observable<File> {
        val converter = object : Function<Response<ResponseBody>, ObservableSource<File>> {
            override fun apply(response: Response<ResponseBody>): ObservableSource<File> {
                return Observable.create(object : ObservableOnSubscribe<File> {
                    override fun subscribe(emitter: ObservableEmitter<File>) {
                        try {
                            val source = response.body()?.source()
                                    ?: throw NullPointerException("download data is empty")
                            val file: File = downloadFile(fileName)
                            file.sink().buffer().apply {
                                writeAll(source)
                                close()
                            }
                            emitter.onNext(file)
                            emitter.onComplete()
                        } catch (e: IOException) {
                            e.printStackTrace()
                            emitter.onError(e)
                        }
                    }
                })
            }
        }
        return service.download(url)
                .flatMap(converter)
    }

}