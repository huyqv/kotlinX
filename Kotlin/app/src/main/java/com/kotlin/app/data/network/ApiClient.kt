package com.kotlin.app.data.network

import com.example.library.extension.parse
import com.kotlin.app.BuildConfig
import com.kotlin.app.data.network.model.Image
import com.kotlin.app.data.network.model.Message
import com.kotlin.app.data.network.model.User
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
class ApiClient {

    private val publicKey: String = "m9Z7mswlRljf8acQ07EesjKOVJDGy2nR0ZrOM22PE40="

    private var token: String? = null

    private val certPinner: CertificatePinner by lazy {
        CertificatePinner.Builder()
                .add(domainName(BuildConfig.SERVICE_URL), "sha256/$publicKey")
                .build()
    }

    private val hasHttps get() = BuildConfig.SERVICE_URL.indexOf("https") == -1

    private var requests: MutableList<Single<*>> = mutableListOf()

    var service: ApiService = initService(ApiService::class, BuildConfig.SERVICE_URL) {
        if (hasHttps) {
            if (!publicKey.isNullOrEmpty()) {
                certificatePinner(certPinner)
            } else {
                trustClient(this)
            }
        }
        if (!token.isNullOrEmpty()) {
            addAuthInterceptor(token)
        }
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