package com.sample.library.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

interface BaseAdapter<T> {

    /**
     * Required implements
     */
    var onItemClick: OnItemClick<T>?

    var onItemLongClick: OnItemClick<T>?

    var onFooterIndexChanged: ((Int) -> Unit)?

    var lastBindIndex: Int

    fun listItem(): List<T>

    fun modelItemOptions(item: T, position: Int): ItemOptions?

    fun ViewBinding.onBindModelItem(item: T, position: Int)

    /**
     * Open implements
     */
    val size: Int get() = listItem().size

    val hasFooter get() = footerItemOptions() != null

    val lastIndex: Int get() = listItem().lastIndex

    val dataIsEmpty: Boolean get() = listItem().isEmpty()

    val dataNotEmpty: Boolean get() = listItem().isNotEmpty()

    val extraItemCount: Int
        get() {
            if (dataIsEmpty && blankItemOptions() != null) 1
            if (dataNotEmpty && footerItemOptions() != null) size + 1
            return size
        }

    fun getItemOrNull(position: Int): T? {
        return listItem().getOrNull(position)
    }

    fun getBaseItemViewType(position: Int): Int {
        blankItemOptions()?.also {
            if (it.layoutId != 0 && dataIsEmpty) {
                return it.layoutId
            }
        }
        footerItemOptions()?.also {
            if (it.layoutId != 0 && dataNotEmpty && position == size) {
                return it.layoutId
            }
        }
        val model = getItemOrNull(position) ?: return 0
        return modelItemOptions(model, position)?.layoutId ?: 0
    }

    fun onBaseCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType != 0) {
            return BaseViewHolder(parent = parent, layoutId = viewType)
        }
        return GoneViewHolder(parent)
    }

    fun onBaseBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val viewType: Int = viewHolder.itemViewType
        if (viewType == blankItemOptions()?.layoutId) {
            return
        }
        if (viewType == footerItemOptions()?.layoutId) {
            return
        }
        if (viewType == 0) {
            return
        }
        val model = getItemOrNull(position) ?: return
        val itemView = viewHolder.itemView
        itemView.addViewClickListener {
            onItemClick?.invoke(model, viewHolder.absoluteAdapterPosition)
        }
        itemView.setOnLongClickListener {
            onItemLongClick?.invoke(model, viewHolder.absoluteAdapterPosition)
            true
        }
        val options = modelItemOptions(model, position) ?: return
        val binding: ViewBinding = options.inflaterInvoker(itemView)
        binding.onBindModelItem(model, position)
        lastBindIndex = position
    }

    fun blankItemOptions(): ItemOptions? = null

    fun footerItemOptions(): ItemOptions? = null

    fun bind(v: RecyclerView, block: (LinearLayoutManager.() -> Unit)? = null) {
        val lm = CenterLayoutManager(v.context)
        block?.invoke(lm)
        v.itemAnimator = DefaultItemAnimator()
        v.layoutManager = lm
        v.adapter = this as RecyclerView.Adapter<*>
    }

    fun bind(v: RecyclerView, spanCount: Int, block: (GridLayoutManager.() -> Unit)? = null) {
        val lm = GridLayoutManager(v.context, spanCount)
        block?.invoke(lm)
        lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (dataIsEmpty || position == size) lm.spanCount
                else 1
            }
        }
        v.itemAnimator = DefaultItemAnimator()
        v.layoutManager = lm
        v.adapter = this as RecyclerView.Adapter<*>
    }

}