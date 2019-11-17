package com.huy.kotlin.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huy.kotlin.R

/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/11
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class BlankAdapter : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_blank, parent, false))
    }

    override fun getItemCount() = 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

}
