package com.kotlin.app.data.api

import android.net.http.X509TrustManagerExtensions
import android.util.Base64
import com.kotlin.app.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URI
import java.net.URISyntaxException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import javax.security.cert.CertificateException

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/21
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object Rest {
    private val trustManager: X509TrustManager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }
    }

    private val sslContext: SSLContext
        get() = SSLContext.getInstance("SSL").also {
            it.init(null, arrayOf(trustManager), SecureRandom())
        }

    @Throws(SSLException::class)
    private fun validatePinning(trustManagerExt: X509TrustManagerExtensions, conn: HttpsURLConnection, validPins: Set<String>) {
        var certChainMsg = ""
        try {
            val md: MessageDigest = MessageDigest.getInstance("SHA-256")
            val trustedChain = trustedChain(trustManagerExt, conn)
            for (cert in trustedChain) {
                val publicKey = cert.publicKey.encoded
                md.update(publicKey, 0, publicKey.size)
                val pin: String = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                certChainMsg += "sha256/$pin : ${cert.subjectDN}"
                if (validPins.contains(pin)) {
                    return
                }
            }
        } catch (e: NoSuchAlgorithmException) {
            throw SSLException(e)
        }
        throw SSLPeerUnverifiedException("Peer certificate chain:$certChainMsg")
    }

    @Throws(SSLException::class)
    private fun trustedChain(trustManagerExt: X509TrustManagerExtensions, conn: HttpsURLConnection): List<X509Certificate> {
        val serverCerts: Array<out Certificate> = conn.serverCertificates
        val untrustedCerts: Array<X509Certificate> = Arrays.copyOf(serverCerts, serverCerts.size, Array<X509Certificate>::class.java)
        val host: String = conn.url.host
        return try {
            trustManagerExt.checkServerTrusted(untrustedCerts, "RSA", host)
        } catch (e: CertificateException) {
            throw SSLException(e)
        }
    }

    @Throws(NoSuchAlgorithmException::class)
    fun String.sha256(): String? {
        val digest = MessageDigest.getInstance("SHA-256").digest(this.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(digest, Base64.NO_WRAP)
    }

    fun trustClient(client: OkHttpClient.Builder) {
        client.sslSocketFactory(sslContext.socketFactory, trustManager)
                .hostnameVerifier(HostnameVerifier { _, _ -> true })
    }

    private val loggingInterceptor: Interceptor
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            return interceptor
        }

    private val client: OkHttpClient.Builder
        get() = OkHttpClient.Builder().also {
            it.connectTimeout(30, TimeUnit.SECONDS)
            it.readTimeout(30, TimeUnit.SECONDS)
            it.writeTimeout(30, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                it.addInterceptor(loggingInterceptor)
            }
        }

    private val retrofit: Retrofit.Builder
        get() = Retrofit.Builder().also {
            it.addConverterFactory(GsonConverterFactory.create())
            it.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        }

    fun initRetrofit(baseURL: String, block: OkHttpClient.Builder.() -> Unit = {}): Retrofit.Builder {
        val newClient = client
        newClient.block()
        return retrofit
                .client(newClient.build())
                .baseUrl(baseURL)
    }

    fun authInterceptor(token: String?): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val request = chain.request().newBuilder()
                if (token != null) {
                    request.addHeader("Authorization", String.format("Bearer %s", token))
                }
                return chain.proceed(request.build())
            }
        }
    }

    fun domainName(url: String): String {
        return try {
            val uri = URI(url)
            val domain = uri.host ?: return ""
            if (domain.startsWith("www.")) domain.substring(4) else domain
        } catch (e: URISyntaxException) {
            ""
        }
    }
}