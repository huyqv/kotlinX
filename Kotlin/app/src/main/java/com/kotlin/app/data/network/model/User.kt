package com.kotlin.app.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/7/5
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
data class User constructor(
        @SerializedName("id") val id: Int,
        @SerializedName("firstName") @Expose val firstName: String?,
        @SerializedName("lastName") val lastName: String,
        @SerializedName("avatar") @Expose val avatar: String?,
        @SerializedName("wallpaper") @Expose val wallpaper: String?,
        @SerializedName("birth") @Expose val birth: Int = 0,
        @SerializedName("gender") @Expose val gender: Int = 0
) {

    fun displayName(): String = if (firstName.isNullOrEmpty()) lastName else "$firstName $lastName"
}