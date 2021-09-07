package com.sample.library.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.*
import androidx.viewbinding.ViewBinding

abstract class BaseListAdapter<T> : ListAdapter<T, RecyclerView.ViewHolder> {

    constructor(itemCallback: DiffUtil.ItemCallback<T> = DiffItemCallback()) : super(itemCallback)

    override fun getItemCount(): Int {
        return size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int /* also it position */): RecyclerView.ViewHolder {
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

    /**
     *
     */
    open var onItemClick: (T, Int) -> Unit = { _, _ -> }

    var onItemLongClick: (T, Int) -> Unit = { _, _ -> }

    var onFooterIndexChanged: (Int) -> Unit = {}

    val size: Int get() = currentList.size

    var lastBindIndex: Int = -1

    val lastIndex: Int get() = currentList.lastIndex

    val dataIsEmpty: Boolean get() = currentList.isEmpty()

    val dataNotEmpty: Boolean get() = currentList.isNotEmpty()

    protected open fun blankInflating(): ItemInflating? = null

    protected open fun footerInflating(): ItemInflating? = null

    protected abstract fun itemInflating(item: T, position: Int): ItemInflating

    protected abstract fun ViewBinding.onBindItem(item: T, position: Int)

    open fun submit() {
        set(currentList)
    }

    open fun get(position: Int): T? {
        return currentList.getOrNull(position)
    }

    open fun set(collection: Collection<T>?) {
        lastBindIndex = -1
        submitList(if (collection != null) ArrayList(collection) else null)
    }

    open fun set(list: MutableList<T>?) {
        lastBindIndex = -1
        submitList(if (list != null) ArrayList(list) else null)
    }

    open fun set(array: Array<T>?) {
        lastBindIndex = -1
        submitList(array?.toMutableList())
    }

    open fun bind(recyclerView: RecyclerView, block: LinearLayoutManager.() -> Unit = {}) {
        val lm = LinearLayoutManager(recyclerView.context)
        lm.block()
        recyclerView.layoutManager = lm
        recyclerView.adapter = this
    }

    open fun bind(recyclerView: RecyclerView, spanCount: Int, block: GridLayoutManager.() -> Unit = {}) {
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