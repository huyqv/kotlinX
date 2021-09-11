package com.kotlin.app.data.network

import com.kotlin.app.shared.serviceUrl
import okhttp3.CertificatePinner

private val publicKey: String = "m9Z7mswlRljf8acQ07EesjKOVJDGy2nR0ZrOM22PE40="

private var token: String? = null

private val certPinner: CertificatePinner by lazy {
    CertificatePinner.Builder()
            .add(domainName(serviceUrl), "sha256/$publicKey")
            .build()
}

val apiService: ApiService by lazy {
    initService(ApiService::class, serviceUrl) {
        if (serviceUrl.indexOf("https") == -1) {
            if (!publicKey.isNullOrEmpty()) {
                certificatePinner(certPinner)
            } else {
                trustClient(this)
            }
        }
        token?.also {
            addInterceptor(authInterceptor(it))
        }
    }
}






