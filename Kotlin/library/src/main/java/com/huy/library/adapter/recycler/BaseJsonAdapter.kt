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

        return if (blankLayoutResource != 0 || footerLayoutResource != 0) size + 1
        else size
    }

    override fun getItemViewType(position: Int): Int {

        if (dataIsEmpty && blankLayoutResource != 0) return blankLayoutResource

        if (dataNotEmpty && footerLayoutResource != 0 && position == size) return footerLayoutResource

        val model = get(position) ?: return 0

        return layoutResource(model, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(onCreateItemView(parent, viewType))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val type = getItemViewType(position)

        if (type == 0) return

        if (type == blankLayoutResource) {
            return
        }

        if (type == footerLayoutResource) {
            if (position.isNotIndexed()) onFooterIndexChange(viewHolder.itemView, position)
            return
        }

        val model = get(position) ?: return

        if (position.isNotIndexed()) viewHolder.itemView.onBindModel(model, position, type)
        else viewHolder.itemView.onFirstBindModel(model, position, type)

        position.updateLastIndex()

        viewHolder.itemView.addViewClickListener {
            onItemClick(model, position)
        }

        viewHolder.itemView.setOnLongClickListener {
            onItemLongClick(model, position)
            return@setOnLongClickListener true
        }

    }


    /**
     * [BaseJsonAdapter] abstractions
     */
    @LayoutRes
    protected abstract fun layoutResource(json: T, position: Int): Int

    protected abstract fun View.onBindModel(json: T, position: Int, @LayoutRes layout: Int)

    open fun View.onFirstBindModel(model: T, position: Int, @LayoutRes layout: Int) {
        onBindModel(model, position, layout)
    }

    open fun onCreateItemView(parent: ViewGroup, viewType: Int): View {
        return if (viewType == 0) {
            View(parent.context).apply { visibility = View.GONE }
        } else {
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        }
    }


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

    var onFooterIndexChange: (View, Int) -> Unit = { _, _ -> }

    fun showFooter(@LayoutRes res: Int) {
        footerLayoutResource = res
        notifyItemChanged(size)
    }

    fun hideFooter() {
        footerLayoutResource = 0
        notifyItemChanged(size)
    }


    /**
     * Item view click
     */
    var onItemClick: (T, Int) -> Unit = { _, _ -> }

    var onItemLongClick: (T, Int) -> Unit = { _, _ -> }


    /**
     * Position
     */
    private var lastIndexPosition: Int = -1

    fun Int.updateLastIndex() {
        if (this > lastIndexPosition) lastIndexPosition = this
    }

    fun Int.isNotIndexed(): Boolean = this > lastIndexPosition

    fun Int.indexInBound(): Boolean = this > -1 && this < size

    fun Int.indexOutBound(): Boolean = this < 0 || this >= size


    /**
     * Data
     */
    val emptyList: JsonArray = JsonArray()

    var data: JsonArray = emptyList
        private set

    val size: Int = data.size()

    val dataIsEmpty: Boolean get() = size == 0

    val dataNotEmpty: Boolean get() = size != 0

    val lastPosition: Int get() = if (dataIsEmpty) -1 else (size - 1)


    /**
     * List update
     */
    open fun get(position: Int): T? {
        if (data.isEmpty()) return null
        @Suppress("UNCHECKED_CAST")
        if (position.indexInBound()) return data[position] as T
        return null
    }

    open fun set(json: String?) {
        val array = json.toArray()
        set(array)
        notifyDataSetChanged()
    }

    open fun set(array: JsonArray?) {
        clear()
        if (array != null && !array.isEmpty()) {
            data.add(array)
        }
        notifyDataSetChanged()
    }

    open fun set(obj: JsonObject?) {
        clear()
        if (obj != null) {
            data.add(obj)
        }
        notifyDataSetChanged()
    }

    open fun setElseEmpty(json: String?) {
        val array = json.toArray() ?: return
        set(array)
        notifyDataSetChanged()
    }

    open fun setElseEmpty(array: JsonArray?) {
        array ?: return
        set(array)
    }

    open fun setElseEmpty(obj: JsonObject?) {
        obj ?: return
        set(obj)
    }

    open fun add(s: String?) {
        add(s.toArray())
    }

    open fun add(array: JsonArray?) {
        array ?: return
        if (array.size() == 0) return
        data.addAll(array)
        notifyDataSetChanged()
    }

    open fun add(obj: JsonObject?) {
        obj ?: return
        data.add(obj)
        notifyDataSetChanged()
    }

    open fun edit(index: Int, model: T?) {
        model ?: return
        if (index.indexInBound()) {
            data[index] = model
            notifyItemChanged(index)
        }
    }

    open fun remove(index: Int) {
        if (index.indexInBound()) {
            data.remove(index)
            notifyDataSetChanged()
        }
    }

    open fun remove(model: T?) {
        model ?: return
        data.remove(model)
        notifyDataSetChanged()
    }

    open fun clear() {
        data = emptyList
        notifyDataSetChanged()
    }


    /**
     * Binding
     */
    open fun bind(recyclerView: RecyclerView, block: (LinearLayoutManager.() -> Unit)? = null) {

        val layoutManager = LinearLayoutManager(recyclerView.context)
        block?.let { layoutManager.block() }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = this
    }

    open fun bind(recyclerView: RecyclerView, spanCount: Int, includeEdge: Boolean = true, block: (GridLayoutManager.() -> Unit)? = null) {

        val layoutManager = GridLayoutManager(recyclerView.context, spanCount)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (dataIsEmpty || position == size) layoutManager.spanCount
                else 1
            }
        }
        block?.let { layoutManager.block() }
        recyclerView.layoutManager = layoutManager
        GridDecoration.draw(recyclerView, layoutManager.spanCount, 0, includeEdge)
        recyclerView.adapter = this
    }


    /**
     * Utils
     */
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

}


