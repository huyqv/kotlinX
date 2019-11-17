package com.huy.kotlin.ui.image

import com.google.gson.annotations.SerializedName
import com.huy.kotlin.ui.image_owner.ImageOwner

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/10/04
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
data class Image(
        @SerializedName("id") val id: Int,
        @SerializedName("url") val url: String

) : ImageOwner {

    override fun getImageUrl() = url
}