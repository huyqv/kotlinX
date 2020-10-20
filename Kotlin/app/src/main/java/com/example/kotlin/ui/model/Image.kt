package com.example.kotlin.ui.model

import com.example.library.adapter.recycler.DiffItemCallback
import com.google.gson.annotations.SerializedName

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
) : ImageOwner() {

    override val imageUrl: String? get() = url

    class ItemCallback : DiffItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.id == newItem.id
        }
    }
}