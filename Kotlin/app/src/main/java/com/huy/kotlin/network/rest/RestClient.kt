package com.huy.kotlin.network.rest

import com.huy.kotlin.BuildConfig
import java.util.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/1/2
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RestClient private constructor() {

    var requestQueue: Queue<Int> = LinkedList()

    var service: RestService = RestHelper.retrofit(BuildConfig.SERVICE_URL)
            .build()
            .create(RestService::class.java)

    companion object {

        val instance: RestClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            RestClient()
        }

        val service: RestService = instance.service

        fun add(tag: Int?) {
            tag ?: return
            instance.requestQueue.add(tag)
        }

        fun remove(tag: Int?) {
            tag ?: return
            instance.requestQueue.remove(tag)
        }

        fun contains(tag: Int?): Boolean {
            tag ?: return false
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