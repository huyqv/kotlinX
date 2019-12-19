package com.huy.kotlin.ui.image

import com.google.gson.annotations.SerializedName
import com.huy.kotlin.base.adapter.DiffItemCallback
import com.huy.kotlin.ui.image_owner.WrapImage

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
) : WrapImage() {

    override val wrapUrl: String? get() = url

    class ItemCallback : DiffItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.id == newItem.id
        }
    }
}