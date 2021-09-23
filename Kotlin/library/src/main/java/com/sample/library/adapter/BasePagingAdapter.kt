package com.sample.library.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.*
import androidx.viewbinding.ViewBinding

abstract class BasePagingAdapter<T : Any> : PagingDataAdapter<T, RecyclerView.ViewHolder> {

    constructor(itemCallback: DiffUtil.ItemCallback<T> = DiffItemCallback()) : super(itemCallback)

    override fun getItemCount(): Int {
        var s = size
        blankItemOptions()?.also { s++ }
        footerItemOptions()?.also { s++ }
        return s
    }

    override fun getItemViewType(position: Int): Int {
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
        val model = get(position) ?: return 0
        return modelItemOptions(model, position)?.layoutId ?: 0
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int /* also it layout resource id */
    ): RecyclerView.ViewHolder {
        if (viewType != 0) {
            return BaseViewHolder(parent = parent, layoutId = viewType)
        }
        return GoneViewHolder(parent)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
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
        val model = get(position) ?: return
        val itemView = viewHolder.itemView
        itemView.addViewClickListener {
            onItemClick(model, viewHolder.absoluteAdapterPosition)
        }
        itemView.setOnLongClickListener {
            onItemLongClick(model, viewHolder.absoluteAdapterPosition)
            true
        }
        val options = modelItemOptions(model, position) ?: return
        val binding: ViewBinding = options.inflaterInvoker(itemView)
        binding.onBindModelItem(model, position)
        lastBindIndex = position
    }

    var onItemClick: (T, Int) -> Unit = { _, _ -> }

    var onItemLongClick: (T, Int) -> Unit = { _, _ -> }

    var onFooterIndexChanged: (Int) -> Unit = {}

    val size: Int get() = snapshot().size

    var lastBindIndex: Int = -1

    val lastIndex: Int get() = snapshot().lastIndex

    val dataIsEmpty: Boolean get() = size == 0

    val dataNotEmpty: Boolean get() = size != 0

    protected open fun blankItemOptions(): ItemOptions? = null

    protected open fun footerItemOptions(): ItemOptions? = null

    protected abstract fun modelItemOptions(item: T, position: Int): ItemOptions?

    protected abstract fun ViewBinding.onBindModelItem(item: T, position: Int)

    open fun get(position: Int): T? {
        return snapshot().getOrNull(position)
    }

    open fun bind(v: RecyclerView, block: (LinearLayoutManager.() -> Unit)? = null) {
        val lm = CenterLayoutManager(v.context)
        block?.invoke(lm)
        v.itemAnimator = DefaultItemAnimator()
        v.layoutManager = lm
        v.adapter = this
    }

    open fun bind(v: RecyclerView, spanCount: Int, block: (GridLayoutManager.() -> Unit)? = null) {
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
        v.adapter = this
    }
}

