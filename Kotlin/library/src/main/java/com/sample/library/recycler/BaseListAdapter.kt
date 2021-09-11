package com.sample.library.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import androidx.viewbinding.ViewBinding

abstract class BaseListAdapter<T> : ListAdapter<T, RecyclerView.ViewHolder> {

    private val differ: AsyncListDiffer<T>

    constructor(itemCallback: DiffUtil.ItemCallback<T> = DiffItemCallback()) : super(itemCallback) {
        differ = asyncListDiffer(itemCallback)
    }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int /* also it layout resource id */): RecyclerView.ViewHolder {
        if (viewType != 0) {
            val v = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            return BaseViewHolder(v)
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
            if (footerItemOptions() != null) s++
            return s
        }

    var lastBindIndex: Int = -1

    val lastIndex: Int get() = currentList.lastIndex

    val dataIsEmpty: Boolean get() = currentList.isEmpty()

    val dataNotEmpty: Boolean get() = currentList.isNotEmpty()

    protected open fun blankItemOptions(): ItemOptions? = null

    protected open fun footerItemOptions(): ItemOptions? = null

    protected abstract fun modelItemOptions(item: T, position: Int): ItemOptions?

    protected abstract fun ViewBinding.onBindModelItem(item: T, position: Int)

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

    open fun setElseEmpty(collection: Collection<T>?, commitCallback: Runnable? = null) {
        if (collection.isNullOrEmpty()) return
        set(collection, commitCallback)
    }

    open fun setElseEmpty(array: Array<T>?, commitCallback: Runnable? = null) {
        if (array.isNullOrEmpty()) return
        set(array, commitCallback)
    }

    private fun asyncListDiffer(itemCallback: DiffUtil.ItemCallback<T>): AsyncListDiffer<T> {

        val adapterCallback = AdapterListUpdateCallback(this)
        val listCallback = object : ListUpdateCallback {
            override fun onChanged(position: Int, count: Int, payload: Any?) {
                adapterCallback.onChanged(position - 1, count + 2, payload)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                adapterCallback.onMoved(fromPosition - 1, toPosition + 1)
            }

            override fun onInserted(position: Int, count: Int) {
                adapterCallback.onInserted(position - 1, count + 2)
            }

            override fun onRemoved(position: Int, count: Int) {
                adapterCallback.onRemoved(position - 1, count + 2)
            }
        }
        return AsyncListDiffer<T>(listCallback, AsyncDifferConfig.Builder<T>(itemCallback).build())
    }

    open fun bind(v: RecyclerView, block: LinearLayoutManager.() -> Unit = {}) {
        val lm = CenterLayoutManager(v.context)
        lm.block()
        v.layoutManager = lm
        v.adapter = this
    }

    open fun bind(v: RecyclerView, spanCount: Int, block: GridLayoutManager.() -> Unit = {}) {
        val lm = GridLayoutManager(v.context, spanCount)
        lm.block()
        lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (dataIsEmpty || position == size) lm.spanCount
                else 1
            }
        }
        v.layoutManager = lm
        v.adapter = this
    }
}