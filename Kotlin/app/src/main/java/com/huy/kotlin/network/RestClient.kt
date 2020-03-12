package com.huy.kotlin.network

import com.huy.kotlin.BuildConfig
import com.huy.kotlin.ui.model.Image
import com.huy.kotlin.ui.model.Message
import com.huy.kotlin.ui.model.User
import com.huy.library.extension.parse
import io.reactivex.Single

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/1/2
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RestClient private constructor() {

    var service: RestService = RestHelper.retrofit(BuildConfig.SERVICE_URL)
            .build()
            .create(RestService::class.java)


    private var requests: MutableList<Single<*>> = mutableListOf()

    private fun <T> Single<T>.filterRequest(): Single<T> {
        return doOnSubscribe {
            requests.add(this)
        }.doOnDispose {
            requests.remove(this)
        }.doOnEvent { _, _ ->
            requests.remove(this)
        }
    }

    companion object {

        val instance: RestClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            RestClient()
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

    fun messages(page: Int): Single<List<Message>?> {
        return service.arrayGET("api/messages$page")
                .filterRequest()
                .map { it.parse(Array<Message>::class.java) }
    }

    fun images(page: Int): Single<List<Image>> {
        return service.arrayGET("api/images$page")
                .filterRequest()
                .map { it.parse(Array<Image>::class.java) }
    }

    fun users(page: Int): Single<List<User>> {
        return service.arrayGET("api/users$page")
                .filterRequest()
                .map { it.parse(Array<User>::class.java) }
    }
}