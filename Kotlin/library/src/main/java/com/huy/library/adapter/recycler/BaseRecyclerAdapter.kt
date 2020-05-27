package com.huy.library.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huy.library.extension.addViewClickListener

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/18
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseRecyclerAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


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

        if (position.isNotIndexed()) viewHolder.itemView.onFirstBindModel(model, position, type)
        else viewHolder.itemView.onBindModel(model, position, type)

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
     * [BaseRecyclerAdapter] abstractions
     */
    @LayoutRes
    protected abstract fun layoutResource(model: T, position: Int): Int

    protected abstract fun View.onBindModel(model: T, position: Int, @LayoutRes layout: Int)

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
    val emptyData: MutableList<T> = mutableListOf()

    var currentList: MutableList<T> = emptyData
        private set

    val size: Int get() = currentList.size

    val dataIsEmpty: Boolean get() = currentList.isEmpty()

    val dataNotEmpty: Boolean get() = currentList.isNotEmpty()

    val lastPosition: Int get() = if (currentList.isEmpty()) -1 else (currentList.size - 1)


    /**
     * List update
     */
    open fun get(position: Int): T? {
        if (position.indexInBound()) return currentList[position]
        return null
    }

    open fun set(collection: Collection<T>?) {
        currentList = collection?.toMutableList() ?: emptyData
        lastIndexPosition = -1
        notifyDataSetChanged()
    }

    open fun set(list: MutableList<T>?) {
        currentList = list ?: emptyData
        lastIndexPosition = -1
        notifyDataSetChanged()
    }

    open fun set(array: Array<T>?) {
        currentList = array?.toMutableList() ?: emptyData
        lastIndexPosition = -1
        notifyDataSetChanged()
    }

    open fun set(model: T?) {
        currentList = if (model == null) emptyData
        else mutableListOf(model)
        lastIndexPosition = -1
        notifyDataSetChanged()
    }

    open fun setElseEmpty(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        currentList = collection.toMutableList()
        lastIndexPosition = -1
        notifyDataSetChanged()
    }

    open fun setElseEmpty(list: MutableList<T>?) {
        if (list.isNullOrEmpty()) return
        currentList = list
        lastIndexPosition = -1
        notifyDataSetChanged()
    }

    open fun setElseEmpty(array: Array<T>?) {
        if (array == null || array.isEmpty()) return
        currentList = array.toMutableList()
        lastIndexPosition = -1
        notifyDataSetChanged()
    }

    open fun setElseEmpty(model: T?) {
        model ?: return
        currentList = mutableListOf(model)
        lastIndexPosition = -1
        notifyDataSetChanged()
    }

    open fun add(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        currentList.addAll(collection)
        notifyDataSetChanged()
    }

    open fun add(array: Array<T>?) {
        if (array == null || array.isEmpty()) return
        currentList.addAll(array)
        notifyDataSetChanged()
    }

    open fun add(model: T?) {
        model ?: return
        currentList.add(model)
        notifyDataSetChanged()
    }

    open fun addFirst(model: T?) {
        model ?: return
        currentList.add(0, model)
        notifyDataSetChanged()
    }

    open fun edit(index: Int, model: T?) {
        model ?: return
        if (index.indexInBound()) {
            currentList[index] = model
            notifyItemChanged(index)
        }
    }

    open fun remove(index: Int) {
        currentList.removeAt(index)
        notifyItemRemoved(index)
    }

    open fun remove(model: T?) {
        model ?: return
        val index = currentList.indexOf(model)
        if (index.indexInBound()) {
            currentList.remove(model)
            notifyItemRemoved(index)
        }
    }

    open fun clear() {
        currentList.clear()
        notifyDataSetChanged()
    }

    open fun unBind() {
        currentList = emptyData
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
