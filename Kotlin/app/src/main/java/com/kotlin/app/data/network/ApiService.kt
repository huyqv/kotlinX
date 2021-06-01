package com.kotlin.app.data.network

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/1/2
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface ApiService {

    companion object {

        private const val PATH_FMT: String = "{path}"

        private const val PATH: String = "path"
    }

    @GET(PATH_FMT)
    fun objectGET(@Path(PATH) path: String): Single<JsonObject>

    @GET(PATH_FMT)
    fun arrayGET(@Path(PATH) path: String): Single<JsonArray>

    @POST(PATH_FMT)
    fun post(@Path(PATH) path: String, @Body body: JsonObject): Single<JsonObject>

    @Multipart
    @POST(PATH_FMT)
    fun post(@Path(PATH) path: String, @Part files: Array<MultipartBody.Part>, @Part("description") des: RequestBody?): Call<ResponseBody>

    @Streaming
    @GET
    fun download(@Url url: String): Observable<Response<ResponseBody>>

    /**
     * sample url: https://concacvip.com/api/user/profile?userId=123456789
     * @param userId: annotated by [Query] (one) or [QueryMap] (multi)
     * @param body: request body from binary/json
     */
    @POST("{path}")
    suspend fun post(
            @Path("path") path: String = "api/user/profile",
            @Query("userId") userId: String = "123456789",
            @Body body: RequestBody = "ccv".toRequestBody(),
    ): JsonObject
}