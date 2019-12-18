package com.huy.kotlin.ui.image

import androidx.recyclerview.widget.DiffUtil
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


    class ItemCallback : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.id === newItem.id
        }

    }
}