package com.huy.library.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.huy.library.extension.addViewClickListener
import com.huy.library.extension.isEmpty
import com.huy.library.extension.toArray

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/18
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseJsonAdapter<T : JsonElement> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    /**
     * [RecyclerView.Adapter] override.
     */
    override fun getItemCount(): Int {
        if (blankLayoutResource != 0 || footerLayoutResource != 0){
            return size + 1
        }
        return size
    }

    override fun getItemViewType(position: Int): Int {
        if (dataIsEmpty && blankLayoutResource != 0){
            return blankLayoutResource
        }
        if (dataNotEmpty && footerLayoutResource != 0 && position == size){
            if (position > lastBindIndex) onFooterIndexChanged(position)
            return footerLayoutResource
        }
        val model = get(position) ?: return 0
        return layoutResource(model, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = if (viewType == 0) {
            View(parent.context).apply { visibility = View.GONE }
        } else {
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        when (type) {
            0, blankLayoutResource, footerLayoutResource -> return
        }
        val model = get(position) ?: return
        viewHolder.itemView.apply {
            onBindModel(model, position, type)
            addViewClickListener {
                onItemClick(model, position)
            }
            setOnLongClickListener {
                onItemLongClick(model, position)
                true
            }
        }
        lastBindIndex = position
    }


    /**
     * [BaseJsonAdapter] abstractions
     */
    @LayoutRes
    protected abstract fun layoutResource(json: T, position: Int): Int

    protected abstract fun View.onBindModel(json: T, position: Int, @LayoutRes layout: Int)


    /**
     * Layout resource for empty data.
     */
    @LayoutRes
    open var blankLayoutResource: Int = 0


    /**
     * Layout resource for footer item.
     */
    @LayoutRes
    open var footerLayoutResource: Int = 0

    var onFooterIndexChanged: (Int) -> Unit = {}

    fun showFooter(@LayoutRes res: Int) {
        footerLayoutResource = res
        notifyItemChanged(size)
    }

    fun hideFooter() {
        showFooter(0)
    }


    /**
     * Item view click
     */
    var onItemClick: (T, Int) -> Unit = { _, _ -> }

    var onItemLongClick: (T, Int) -> Unit = { _, _ -> }

    /**
     * Data
     */
    private var currentList: JsonArray? = null

    val size: Int = currentList?.size() ?: 0

    var lastBindIndex: Int = -1

    val lastIndex: Int get() = size - 1

    val dataIsEmpty: Boolean get() = size == 0

    val dataNotEmpty: Boolean get() = size != 0

    /**
     * List update
     */
    open fun get(position: Int): T? {
        if (currentList.isEmpty()) return null
        @Suppress("UNCHECKED_CAST")
        if (position in 0..lastIndex) return currentList?.get(position) as? T
        return null
    }

    open fun set(json: String?) {
        val array = json.toArray()
        set(array)
        lastBindIndex = -1
        notifyDataSetChanged()
    }

    open fun set(array: JsonArray?) {
        currentList = array
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
        currentList?.addAll(array)
        notifyDataSetChanged()
    }

    open fun add(obj: JsonObject?) {
        obj ?: return
        val array = currentList ?: JsonArray()
        array.add(obj)
        currentList = array
        notifyDataSetChanged()
    }

    open fun edit(position: Int, model: JsonObject?) {
        model ?: return
        if (position in 0..lastIndex) currentList?.apply {
            currentList!![position] = model
            notifyItemChanged(position)
        }
    }

    open fun remove(position: Int) {
        if (position in 0..lastIndex) currentList?.apply {
            remove(position)
            notifyItemRemoved(position)
        }
    }

    open fun remove(model: T?) {
        model ?: return
        val position = currentList?.indexOf(model) ?: -1
        remove(position)
    }

    open fun clear() {
        currentList = null
        notifyDataSetChanged()
    }

}


