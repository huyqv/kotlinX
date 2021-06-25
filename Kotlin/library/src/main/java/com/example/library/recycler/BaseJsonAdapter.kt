package com.example.library.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.library.extension.addViewClickListener
import com.example.library.extension.isEmpty
import com.example.library.extension.toArray
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

abstract class BaseJsonAdapter<T : JsonElement> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
    var onItemClick: (T, Int) -> Unit = { _, _ -> }

    var onItemLongClick: (T, Int) -> Unit = { _, _ -> }

    var onFooterIndexChanged: (Int) -> Unit = {}

    private var currentList: JsonArray = JsonArray()

    val size: Int = currentList.size()

    var lastBindIndex: Int = -1

    val lastIndex: Int get() = size - 1

    val dataIsEmpty: Boolean get() = size == 0

    val dataNotEmpty: Boolean get() = size != 0

    protected open fun blankInflating(): ItemInflating? = null

    protected open fun footerInflating(): ItemInflating? = null

    protected abstract fun itemInflating(item: T, position: Int): ItemInflating

    protected abstract fun ViewBinding.onBindItem(item: T, position: Int)

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


