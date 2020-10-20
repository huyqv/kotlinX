package com.example.kotlin.data.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

}