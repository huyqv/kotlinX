package com.huy.kotlin.network.rest

import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.huy.kotlin.ui.model.Image
import com.huy.kotlin.ui.model.Message
import com.huy.kotlin.ui.model.User
import io.reactivex.Observable
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

    @GET("api/images{page}")
    fun imageLiveData(@Path("page") page: Int): LiveData<RestResponse<List<Image>>>

    @GET("api/messages{page}")
    fun messages(@Path("page") page: Int): Single<List<Message>?>

    @GET("api/messages{page}")
    fun messagesCall(@Path("page") page: Int): Call<List<Message>?>

    @GET("api/images{page}")
    fun images(@Path("page") page: Int): Observable<List<Image>>

    @GET("api/users{page}")
    fun users(@Path("page") page: Int): Single<List<User>>

    @GET("api/images{page}")
    fun call(@Path("page") page: Int): Call<List<Image>?>

    @GET("api/images{page}")
    fun single(@Path("page") page: Int): Single<List<Image>>

    @GET("api/images{page}")
    fun observable(@Path("page") page: Int): Observable<List<Image>>

    @GET("")
    fun get(@Query("id") map: String): Call<JsonObject>

    @GET("")
    fun get(@QueryMap map: Map<String, Any>): Call<JsonObject>

    @GET("")
    fun get(@Body body: JsonObject): Call<JsonObject>

    @GET("")
    fun get(): Call<JsonObject>

    @FormUrlEncoded
    @POST("")
    fun post(@Field("id") s: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("")
    fun post(@FieldMap map: Map<String, Any>): Call<JsonObject>

    @POST("")
    fun post(@Body body: JsonObject): Call<JsonObject>

    @POST("")
    @Multipart
    fun upload(@Part file: MultipartBody.Part): Call<JsonObject>

    @POST("")
    @Multipart
    fun upload(@Part file: MultipartBody.Part, @Part("description") des: RequestBody): Call<JsonObject>

    @POST("")
    @Multipart
    fun upload(@Part files: Array<MultipartBody.Part>): Call<ResponseBody>

    @GET("")
    fun liveData(@Body body: JsonObject): LiveData<RestResponse<JsonObject>>

    @GET("")
    fun request(@Body body: JsonObject): Observable<JsonObject>

}