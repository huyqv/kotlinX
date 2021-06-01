package com.kotlin.app.data.network

import com.kotlin.app.BuildConfig
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

}