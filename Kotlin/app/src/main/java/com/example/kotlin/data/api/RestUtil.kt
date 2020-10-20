package com.example.kotlin.data.api

import com.example.kotlin.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URI
import java.net.URISyntaxException
import java.util.concurrent.TimeUnit

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/07/21
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object RestUtil {

    private val loggingInterceptor: Interceptor
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            return interceptor
        }

    fun createService(baseURL: String, block: OkHttpClient.Builder.() -> Unit = {}): Retrofit.Builder {
        val client = OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
        }
        client.block()
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client.build())
                .baseUrl(baseURL)
    }

    fun getHeaderInterceptor(token: String?): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder()
                if (token != null) {
                    request.addHeader("Authorization", String.format("Bearer %s", token))
                }
                return chain.proceed(request.build())
            }
        }
    }

    fun getDomainName(url: String): String {
        return try {
            val uri = URI(url)
            val domain = uri.host ?: return ""
            if (domain.startsWith("www.")) domain.substring(4) else domain
        } catch (e: URISyntaxException) {
            ""
        }
    }

}