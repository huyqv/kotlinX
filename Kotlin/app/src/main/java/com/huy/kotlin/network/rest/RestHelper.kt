package com.huy.kotlin.network.rest

import com.huy.kotlin.network.callback.LiveDataCallAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

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

    fun retrofit(baseURL: String): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .baseUrl(baseURL)
    }

    fun getClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
    }

    fun getLoggingInterceptor(enable: Boolean): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (enable) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return interceptor
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