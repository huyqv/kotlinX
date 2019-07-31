package com.huy.kotlin.network.callback

import androidx.lifecycle.LiveData
import com.google.gson.JsonElement
import com.huy.kotlin.network.rest.RestResponse
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/11/24
 * @Description:
 * Use with
 *          var service: RestService = Retrofit.Builder()
 *                  .addCallAdapterFactory(LiveDataCallAdapterFactory())
 *                  .baseUrl(BuildConfig.SERVICE_URL)
 *                  .build()
 *                  .create(RestService::class.java)
 * And
 *          @GET("api-url")
 *          fun imageLiveData(): LiveData<RestResponse<T>>
 *
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class LiveDataCallAdapterFactory : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class.java)
            return null

        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        val observableClass = getRawType(observableType)

        if (observableClass != RestResponse::class.java)
            throw IllegalArgumentException("type must be a resource")

        if (observableType !is ParameterizedType)
            throw IllegalArgumentException("resource must be parameterized")

        val bodyType = getParameterUpperBound(0, observableType)
        return LiveDataAdapter<JsonElement>(bodyType)
    }

    class LiveDataAdapter<T>(private val responseType: Type) : CallAdapter<T, LiveData<RestResponse<T>>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<T>?): LiveData<RestResponse<T>> {
            return object : LiveData<RestResponse<T>>() {
                var started = AtomicBoolean(false)
                override fun onActive() {
                    super.onActive()
                    if (!started.compareAndSet(false, true))
                        return
                    call?.enqueue(object : Callback<T> {
                        override fun onFailure(call: Call<T>?, throwable: Throwable?) {
                            postValue(RestResponse(throwable))
                        }

                        override fun onResponse(call: Call<T>?, response: Response<T>?) {
                            postValue(RestResponse(response))
                        }

                    })
                }
            }
        }
    }
}