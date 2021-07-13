package com.kotlin.app.data.network

import com.kotlin.app.shared.serviceUrl
import io.reactivex.Single
import okhttp3.CertificatePinner

class ApiClient {

    private val publicKey: String = "m9Z7mswlRljf8acQ07EesjKOVJDGy2nR0ZrOM22PE40="

    private var token: String? = null

    private val certPinner: CertificatePinner by lazy {
        CertificatePinner.Builder()
            .add(domainName(serviceUrl), "sha256/$publicKey")
            .build()
    }

    private val hasHttps get() = serviceUrl.indexOf("https") == -1

    private var requests: MutableList<Single<*>> = mutableListOf()

    var service: ApiService = initService(ApiService::class, serviceUrl) {
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