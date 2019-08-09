package com.huy.kotlin.network.rest

import com.huy.kotlin.BuildConfig
import com.huy.kotlin.app.LOG
import com.huy.library.Logger
import java.util.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/1/2
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RestClient private constructor() : Logger(RestClient::class.java, LOG) {

    /**
     * Linked list of post code
     * Guaranteed api post job as a post execute if it being not executing
     */
    var requestQueue: Queue<String> = LinkedList()

    var service: RestService = RestHelper.retrofit(BuildConfig.SERVICE_URL)
            .build()
            .create(RestService::class.java)

    companion object {

        val instance: RestClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            RestClient()
        }

        val service: RestService = instance.service

        fun add(tag: String?) {
            tag ?: return
            instance.requestQueue.add(tag)
        }

        fun remove(tag: String?) {
            tag ?: return
            instance.requestQueue.remove(tag)
        }

        fun contains(tag: String): Boolean {
            return instance.requestQueue.contains(tag)
        }

        fun setToken(token: String? = null) {

            val client = RestHelper.getClient()
                    .addInterceptor(RestHelper.getLoggingInterceptor(BuildConfig.DEBUG))

            if (token != null) {
                client.addInterceptor(RestHelper.getHeaderInterceptor(token))
            }
            instance.service = RestHelper.retrofit(BuildConfig.SERVICE_URL)
                    .client(client.build())
                    .build()
                    .create(RestService::class.java)
        }

    }

}