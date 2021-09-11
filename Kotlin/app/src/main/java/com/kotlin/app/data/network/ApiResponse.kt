package com.kotlin.app.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiResponse<T> {

    @SerializedName("code")
    var code: Int = 0

    @SerializedName("message")
    @Expose
    var message: String = "Something went wrong :( !"

    @SerializedName("body")
    @Expose
    var body: T? = null

}