package com.example.kotlin.ui.model

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
data class Message(
        @SerializedName("id") val id: Int,
        @SerializedName("firstName") @Expose val firstName: String?,
        @SerializedName("lastName") val lastName: String,
        @SerializedName("avatar") @Expose val avatar: String?,
        @SerializedName("text") @Expose val text: String?,
        @SerializedName("image") @Expose val image: String?,
        @SerializedName("url") @Expose val url: String?,
        @SerializedName("sticker") @Expose val sticker: String?,
        @SerializedName("time") @Expose var time: Long = 0
) : ImageOwner() {

    override val imageUrl: String? get() = url

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldConcert: Message, newConcert: Message): Boolean {
                return oldConcert.id == newConcert.id
            }

            override fun areContentsTheSame(oldConcert: Message, newConcert: Message): Boolean {
                return oldConcert == newConcert
            }
        }
    }
}