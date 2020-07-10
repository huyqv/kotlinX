package com.huy.kotlin.network

import android.util.Base64
import com.huy.kotlin.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URI
import java.net.URISyntaxException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/07/21
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object RestHelper {

    private const val CONTENT_TYPE = "Content-Type"

    private const val AUTHORIZATION = "Authorization"

    private const val API_KEY = "ApiKey"

    /**
     * Base64 string sha256
     */
    private const val PUBLIC_KEY = "123456="

    private var hasPinning: Boolean = false

    private var certPinner: CertificatePinner = CertificatePinner.Builder()
            .add(BuildConfig.SERVICE_URL.getDomainName(), "sha256/$PUBLIC_KEY")
            .build()

    private val trustManager: Array<TrustManager> = arrayOf(object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }
    })

    private val sslContext: SSLContext
        get() = SSLContext.getInstance("SSL").also {
            it.init(null, trustManager, SecureRandom())
        }

    /**
     * Logging Interceptor
     */
    val loggingInterceptor: Interceptor
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            return interceptor
        }

    /**
     * OkHttpClient
     */
    private val client: OkHttpClient.Builder
        get() {
            val client = OkHttpClient.Builder()
            if (BuildConfig.SERVICE_URL.indexOf("https") != -1 && hasPinning) {
                client.certificatePinner(certPinner)
                client.sslSocketFactory(sslContext.socketFactory, trustManager[0] as X509TrustManager)
                        .hostnameVerifier(HostnameVerifier { hostname, _ /*session*/ ->
                            hostname == BuildConfig.SERVICE_URL.getDomainName()
                        })
            }
            if (BuildConfig.DEBUG) {
                client.addInterceptor(loggingInterceptor)
            }
            return client
        }


    /**
     * https://www.developer.android.com/studio -> developer.android.com
     */
    private fun String.getDomainName(): String {
        return try {
            val uri = URI(this)
            val domain = uri.host ?: return ""
            if (domain.startsWith("www.")) domain.substring(4) else domain
        } catch (e: URISyntaxException) {
            ""
        }
    }

    /**
     * RestFUL API factory
     */
    fun createService(baseURL: String, block: OkHttpClient.Builder.() -> Unit = {}): Retrofit.Builder {
        val newClient = client
        newClient.block()
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client.build())
                .baseUrl(baseURL)
    }


    @Throws(NoSuchAlgorithmException::class)
    fun String.sha256(): String? {
        val digest = MessageDigest.getInstance("SHA-256").digest(this.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(digest, Base64.NO_WRAP)
    }

    fun getAuthenticator(): Authenticator {
        return object : Authenticator {
            override fun authenticate(route: Route?, response: Response): Request? {
                return if (!response.request.header(AUTHORIZATION).equals("")) null else null
            }
        }
    }

    fun getHeaderInterceptor(token: String?): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder()
                if (token != null) {
                    request.addHeader(AUTHORIZATION, String.format("Bearer %s", token))
                }
                return chain.proceed(request.build())
            }
        }
    }

}