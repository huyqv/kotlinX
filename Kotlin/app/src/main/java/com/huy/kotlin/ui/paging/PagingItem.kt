package com.huy.kotlin.ui.paging

import androidx.recyclerview.widget.DiffUtil

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/11
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class PagingItem(val id: Int) {

    override fun equals(other: Any?): Boolean {
        return id == (other as PagingItem).id
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PagingItem>() {
            override fun areItemsTheSame(oldConcert: PagingItem, newConcert: PagingItem): Boolean {
                return oldConcert.id == newConcert.id
            }

            override fun areContentsTheSame(oldConcert: PagingItem, newConcert: PagingItem): Boolean {
                return oldConcert == newConcert
            }
        }
    }
}