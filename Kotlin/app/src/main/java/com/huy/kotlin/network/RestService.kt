package com.huy.kotlin.network

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/1/2
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface RestService {

    @GET("{path}")
    fun objectGET(@Path("{path}") path: String
    ): Single<JsonObject>

    @GET("{path}")
    fun arrayGET(@Path("{path}") path: String
    ): Single<JsonArray>

    @POST("{path}")
    fun objectPOST(@Path("{path}") path: String,
                   @Body body: JsonObject
    ): Single<JsonObject>

    @POST("{path}")
    fun arrayPOST(@Path("{path}") path: String,
                  @Body body: JsonObject
    ): Single<JsonArray>

    @Multipart
    @POST("{path}")
    fun upload(@Path("{path}") path: String,
               @Part files: Array<MultipartBody.Part>,
               @Part("description") des: RequestBody?
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("{path}")
    fun post(@Path("{path}") path: String,
             @FieldMap map: Map<String, Any>
    ): Call<JsonObject>

}