package com.sample.library.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * The adapter to assist the [StickyHeaderDecoration] in creating and binding the header views.
 */
interface StickyHeaderAdapter<T : RecyclerView.ViewHolder> {

    fun getHeaderId(position: Int): Long

    fun onCreateHeaderViewHolder(parent: ViewGroup, position: Int): T

    fun onBindHeaderViewHolder(viewHolder: T, position: Int)

    companion object {
        const val NO_HEADER_ID = -1L
    }
}