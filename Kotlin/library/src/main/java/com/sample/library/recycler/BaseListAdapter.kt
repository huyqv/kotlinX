package com.sample.library.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.*
import androidx.viewbinding.ViewBinding

abstract class BaseListAdapter<T> : ListAdapter<T, RecyclerView.ViewHolder> {

    private val differ: AsyncListDiffer<T>

    constructor(itemCallback: DiffUtil.ItemCallback<T> = DiffItemCallback()) : super(itemCallback) {
        differ = asyncListDiffer(itemCallback)
    }

    override fun getItemCount(): Int {
        return size
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

    override fun getCurrentList(): MutableList<T> {
        return differ.currentList
    }

    override fun submitList(list: MutableList<T>?) {
        differ.submitList(list)
    }

    override fun submitList(list: MutableList<T>?, commitCallback: Runnable?) {
        differ.submitList(list, commitCallback)
    }


    /**
     *
     */
    open var onItemClick: (T, Int) -> Unit = { _, _ -> }

    open var onItemLongClick: (T, Int) -> Unit = { _, _ -> }

    var onFooterIndexChanged: (Int) -> Unit = {}

    val size: Int
        get() {
            var s = currentList.size
            if (footerInflating() != null) s++
            return s
        }

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

    open fun set(collection: Collection<T>?, commitCallback: Runnable? = null) {
        lastBindIndex = -1
        submitList(collection?.toMutableList(), commitCallback)
    }

    open fun set(array: Array<T>?, commitCallback: Runnable? = null) {
        lastBindIndex = -1
        submitList(array?.toMutableList(), commitCallback)
    }

    private fun asyncListDiffer(itemCallback: DiffUtil.ItemCallback<T>): AsyncListDiffer<T> {

        val adapterCallback = AdapterListUpdateCallback(this)
        val listCallback = object : ListUpdateCallback {
            override fun onChanged(position: Int, count: Int, payload: Any?) {
                adapterCallback.onChanged(position + 1, count, payload)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                adapterCallback.onMoved(fromPosition + 1, toPosition + 1)
            }

            override fun onInserted(position: Int, count: Int) {
                adapterCallback.onInserted(position + 1, count + 1)
            }

            override fun onRemoved(position: Int, count: Int) {
                adapterCallback.onRemoved(position + 1, count)
            }
        }
        return AsyncListDiffer<T>(listCallback, AsyncDifferConfig.Builder<T>(itemCallback).build())
    }

    open fun bind(recyclerView: RecyclerView, block: LinearLayoutManager.() -> Unit = {}) {
        val lm = CenterLayoutManager(recyclerView.context)
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