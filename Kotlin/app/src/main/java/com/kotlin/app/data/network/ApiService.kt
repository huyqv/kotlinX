package com.kotlin.app.data.network

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    companion object {

        private const val PATH_FMT: String = "{path}"

        private const val PATH: String = "path"
    }

    @GET(PATH_FMT)
    suspend fun objectGET(@Path(PATH) path: String): JsonObject

    @GET(PATH_FMT)
    suspend fun arrayGET(@Path(PATH) path: String): JsonArray

    @POST(PATH_FMT)
    suspend fun post(@Path(PATH) path: String, @Body body: JsonObject): JsonObject

    @Multipart
    @POST(PATH_FMT)
    suspend fun post(
        @Path(PATH) path: String,
        @Part files: Array<MultipartBody.Part>,
        @Part("description") des: RequestBody?
    ): Call<ResponseBody>

    @Streaming
    @GET
    suspend fun download(@Url url: String): Response<ResponseBody>

    /**
     * sample url: https://sample.com/api/user/profile?userId=123456789
     * @param userId: annotated by [Query] (one) or [QueryMap] (multi)
     * @param body: request body from binary/json
     */
    @POST(PATH_FMT)
    suspend fun post(
        @Path(PATH) path: String = "api/user/profile",
        @Query("userId") userId: String = "123456789",
        @Body body: RequestBody = "ccv".toRequestBody(),
    ): JsonObject
}