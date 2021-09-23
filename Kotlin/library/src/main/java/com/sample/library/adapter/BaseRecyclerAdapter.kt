package com.sample.library.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    /**
     *
     */
    open var onItemClick: (T, Int) -> Unit = { _, _ -> }

    var onItemLongClick: (T, Int) -> Unit = { _, _ -> }

    var onFooterIndexChanged: (Int) -> Unit = {}

    var currentList: MutableList<T> = mutableListOf()

    var lastBindIndex: Int = -1

    val lastIndex: Int get() = currentList.lastIndex

    val size: Int get() = currentList.size

    val dataIsEmpty: Boolean get() = currentList.isNullOrEmpty()

    val dataNotEmpty: Boolean get() = !dataIsEmpty

    protected open fun blankItemOptions(): ItemOptions? = null

    protected open fun footerItemOptions(): ItemOptions? = null

    protected abstract fun modelItemOptions(item: T, position: Int): ItemOptions?

    protected abstract fun ViewBinding.onBindModelItem(item: T, position: Int)

    open fun get(position: Int): T? {
        return currentList.getOrNull(position)
    }

    open fun set(collection: Collection<T>?) {
        currentList = collection?.toMutableList() ?: mutableListOf()
        Log.d("BRA", "=====================")
        currentList.forEach {
            Log.d("BRA", it.toString())
        }
        lastBindIndex = -1
        //notifyItemRangeChanged(0, itemCount-1)
        notifyDataSetChanged()
    }

    open fun set(array: Array<T>?) {
        currentList = array?.toMutableList() ?: mutableListOf()
        currentList.forEach {
            Log.d("BRA", it.toString())
        }
        lastBindIndex = -1
        notifyDataSetChanged()
    }

    open fun setElseEmpty(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        set(collection)
    }

    open fun setElseEmpty(array: Array<T>?) {
        if (array == null || array.isEmpty()) return
        set(array)
    }

    open fun add(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        currentList.addAll(collection)
        notifyDataSetChanged()
    }

    open fun add(array: Array<T>?) {
        if (array.isNullOrEmpty()) return
        currentList.addAll(array)
        notifyDataSetChanged()
    }

    open fun add(model: T?) {
        model ?: return
        currentList.add(model)
        notifyItemRangeChanged(size, size + 1)
    }

    open fun add(position: Int, model: T?) {
        model ?: return
        currentList.add(position, model)
        notifyDataSetChanged()
    }

    open fun edit(position: Int, model: T?) {
        model ?: return
        if (position in 0..lastIndex) {
            currentList[position] = model
            notifyItemChanged(position)
        }
    }

    open fun remove(index: Int) {
        currentList.removeAt(index)
        notifyItemRemoved(index)
    }

    open fun remove(model: T?) {
        model ?: return
        val position = currentList.indexOf(model)
        remove(position)
    }

    open fun clear() {
        currentList = mutableListOf()
        notifyDataSetChanged()
    }

    open fun bind(v: RecyclerView, block: LinearLayoutManager.() -> Unit = {}) {
        val lm = LinearLayoutManager(v.context)
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