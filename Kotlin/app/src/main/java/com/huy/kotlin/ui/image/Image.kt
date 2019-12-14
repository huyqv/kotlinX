package com.huy.kotlin.ui.image

import com.google.gson.annotations.SerializedName
import com.huy.kotlin.ui.image_owner.ImageOwner
import com.huy.library.extension.SimpleDiffCallback

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

    class DiffCallback(oldList: Collection<Image>, newList: Collection<Image>) : SimpleDiffCallback<Image>(oldList, newList) {
        override fun areItemSame(old: Image, new: Image) = old.url === new.url
    }
}