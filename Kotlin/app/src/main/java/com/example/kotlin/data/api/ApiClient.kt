package com.example.kotlin.data.api

import com.example.kotlin.BuildConfig
import com.example.kotlin.ui.model.Image
import com.example.kotlin.ui.model.Message
import com.example.kotlin.ui.model.User
import com.example.library.extension.parse
import io.reactivex.Single
import okhttp3.CertificatePinner

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/1/2
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
val apiClient: ApiClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    ApiClient()
}

class ApiClient {

    private val publicKey: String = "m9Z7mswlRljf8acQ07EesjKOVJDGy2nR0ZrOM22PE40="

    private val certPinner: CertificatePinner by lazy {
        CertificatePinner.Builder()
                .add(Rest.domainName(BuildConfig.SERVICE_URL), "sha256/$publicKey")
                .build()
    }

    val hasPinning: Boolean get() = !publicKey.isNullOrEmpty()

    var service: ApiService = Rest.initRetrofit(BuildConfig.SERVICE_URL) {
        if (BuildConfig.SERVICE_URL.indexOf("https") == -1) {
            if (hasPinning) {
                certificatePinner(certPinner)
            } else {
                Rest.trustClient(this)
            }
        }
    }
            .build()
            .create(ApiService::class.java)


    private var requests: MutableList<Single<*>> = mutableListOf()

    fun setToken(token: String? = null) {
        apiClient.service = Rest.initRetrofit(BuildConfig.SERVICE_URL) {
            if (token != null) {
                addInterceptor(Rest.authInterceptor(token))
            }
        }
                .build()
                .create(ApiService::class.java)
    }

    private fun <T> Single<T>.filterRequest(): Single<T> {
        return doOnSubscribe {
            requests.add(this)
        }.doOnDispose {
            requests.remove(this)
        }.doOnEvent { _, _ ->
            requests.remove(this)
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

}