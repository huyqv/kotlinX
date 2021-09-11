package com.sample.library.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.sample.library.extension.isEmpty
import com.sample.library.extension.toArray

abstract class BaseJsonAdapter<T : JsonElement> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    /**
     *
     */
    var onItemClick: (T, Int) -> Unit = { _, _ -> }

    var onItemLongClick: (T, Int) -> Unit = { _, _ -> }

    var onFooterIndexChanged: (Int) -> Unit = {}

    private var currentList: JsonArray = JsonArray()

    val size: Int = currentList.size()

    var lastBindIndex: Int = -1

    val lastIndex: Int get() = size - 1

    val dataIsEmpty: Boolean get() = size == 0

    val dataNotEmpty: Boolean get() = size != 0

    protected open fun blankItemOptions(): ItemOptions? = null

    protected open fun footerItemOptions(): ItemOptions? = null

    protected abstract fun modelItemOptions(item: T, position: Int): ItemOptions?

    protected abstract fun ViewBinding.onBindModelItem(item: T, position: Int)

    open fun get(position: Int): T? {
        if (currentList.isEmpty()) return null
        @Suppress("UNCHECKED_CAST")
        if (position in 0..lastIndex) return currentList.get(position) as? T
        return null
    }

    open fun set(json: String?) {
        val array = json.toArray()
        set(array)
        lastBindIndex = -1
        notifyDataSetChanged()
    }

    open fun set(array: JsonArray?) {
        currentList = array ?: JsonArray()
        lastBindIndex = -1
        notifyDataSetChanged()
    }

    open fun setElseEmpty(json: String?) {
        val array = json.toArray() ?: return
        set(array)
    }

    open fun setElseEmpty(array: JsonArray?) {
        array ?: return
        set(array)
    }

    open fun add(s: String?) {
        add(s.toArray())
    }

    open fun add(array: JsonArray?) {
        array ?: return
        if (array.isEmpty()) return
        currentList.addAll(array)
        notifyDataSetChanged()
    }

    open fun add(obj: JsonObject?) {
        obj ?: return
        currentList.add(obj)
        notifyDataSetChanged()
    }

    open fun edit(position: Int, model: JsonObject?) {
        model ?: return
        if (position in 0..lastIndex) {
            currentList[position] = model
            notifyItemChanged(position)
        }
    }

    open fun remove(position: Int) {
        if (position in 0..lastIndex) {
            currentList.remove(position)
            notifyItemRemoved(position)
        }
    }

    open fun remove(model: T?) {
        model ?: return
        remove(currentList.indexOf(model))
    }

    open fun clear() {
        currentList = JsonArray()
        notifyDataSetChanged()
    }

    open fun bind(recyclerView: RecyclerView, block: LinearLayoutManager.() -> Unit = {}) {
        val lm = LinearLayoutManager(recyclerView.context)
        lm.block()
        recyclerView.layoutManager = lm
        recyclerView.adapter = this
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