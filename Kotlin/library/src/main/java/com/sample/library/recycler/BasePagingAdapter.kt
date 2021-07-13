package com.sample.library.recycler

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BasePagingAdapter<T : Any> : PagingDataAdapter<T, RecyclerView.ViewHolder> {

    constructor(itemCallback: ItemCallback<T> = DiffItemCallback()) : super(itemCallback)

    override fun getItemCount(): Int {
        return size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int /* also it position */
    ): RecyclerView.ViewHolder {
        when {
            dataIsEmpty -> blankInflating().invokeItem(parent)?.also {
                return BaseViewHolder(it)
            }
            dataNotEmpty && viewType == size -> footerInflating().invokeItem(parent)?.also {
                if (viewType > lastBindIndex) onFooterIndexChanged(viewType)
                return BaseViewHolder(it)
            }
            else -> get(viewType)?.also { item ->
                itemInflating(item, viewType).invokeItem(parent)?.also {
                    return BaseViewHolder(it)
                }
            }
        }
        return GoneViewHolder(parent)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val model = get(position) ?: return
        when (viewHolder) {
            is BaseViewHolder<*> -> viewHolder.bind.apply {
                onBindItem(model, position)
                root.addViewClickListener {
                    onItemClick(model, position)
                }
                root.setOnLongClickListener {
                    onItemLongClick(model, position)
                    true
                }
                lastBindIndex = position
            }
        }
    }

    @LayoutRes
    protected abstract fun layoutResource(model: T, position: Int): Int

    protected abstract fun View.onBindModel(model: T, position: Int, @LayoutRes layout: Int)

    var onItemClick: (T, Int) -> Unit = { _, _ -> }

    var onItemLongClick: (T, Int) -> Unit = { _, _ -> }

    var onFooterIndexChanged: (Int) -> Unit = {}

    val size: Int get() = snapshot().size

    var lastBindIndex: Int = -1

    val lastIndex: Int get() = snapshot().lastIndex

    val dataIsEmpty: Boolean get() = size == 0

    val dataNotEmpty: Boolean get() = size != 0

    protected open fun blankInflating(): ItemInflating? = null

    protected open fun footerInflating(): ItemInflating? = null

    protected abstract fun itemInflating(item: T, position: Int): ItemInflating

    protected abstract fun ViewBinding.onBindItem(item: T, position: Int)

    open fun get(position: Int): T? {
        return snapshot().getOrNull(position)
    }

    open fun bind(recyclerView: RecyclerView, block: LinearLayoutManager.() -> Unit = {}) {
        val lm = LinearLayoutManager(recyclerView.context)
        lm.block()
        recyclerView.layoutManager = lm
        recyclerView.adapter = this
        addLoadStateListener {
            //val retryVisible = it.refresh is LoadState.Error
            //val swipeRefreshLayoutIsRefreshing = it.refresh is LoadState.Loading
            //val emptyStateIsVisible = it.refresh is LoadState.Loading && itemCount == 0
        }
    }

    open fun bind(
        recyclerView: RecyclerView,
        spanCount: Int,
        block: GridLayoutManager.() -> Unit = {}
    ) {
        val lm = GridLayoutManager(recyclerView.context, spanCount)
        lm.block()
        lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (dataIsEmpty || position == size) lm.spanCount
                else 1
            }
        }
        recyclerView.layoutManager = lm
        recyclerView.adapter = this
    }

}

