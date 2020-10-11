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
            if (position.isNotIndexed) onFooterIndexChange(viewHolder.itemView, position)
            return
        }

        val model = get(position) ?: return

        if (position.isNotIndexed) viewHolder.itemView.onFirstBindModel(model, position, type)
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
     * Data
     */
    val emptyData: MutableList<T> = mutableListOf()

    var currentList: MutableList<T>? = null

    val lastIndex: Int get() = currentList?.lastIndex ?: -1

    val size: Int get() = currentList.size

    val dataIsEmpty: Boolean get() = currentList.isEmpty()

    val dataNotEmpty: Boolean get() = currentList.isNotEmpty()

    val lastPosition: Int get() = if (currentList.isEmpty()) -1 else (currentList.size - 1)


    /**
     * List update
     */
    open fun get(position: Int): T? {
        if (position in 0..lastIndex) return currentList[position]
        return null
    }

    open fun set(collection: Collection<T>?) {
        currentList = collection?.toMutableList() ?: emptyData
        notifyDataSetChanged()
    }

    open fun set(list: MutableList<T>?) {
        currentList = list ?: emptyData
        notifyDataSetChanged()
    }

    open fun set(array: Array<T>?) {
        currentList = array?.toMutableList() ?: emptyData
        notifyDataSetChanged()
    }

    open fun set(model: T?) {
        currentList = if (model == null) emptyData
        else mutableListOf(model)
        notifyDataSetChanged()
    }

    open fun setElseEmpty(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        currentList = collection.toMutableList()
        notifyDataSetChanged()
    }

    open fun setElseEmpty(list: MutableList<T>?) {
        if (list.isNullOrEmpty()) return
        currentList = list
        notifyDataSetChanged()
    }

    open fun setElseEmpty(array: Array<T>?) {
        if (array == null || array.isEmpty()) return
        currentList = array.toMutableList()
        notifyDataSetChanged()
    }

    open fun setElseEmpty(model: T?) {
        model ?: return
        currentList = mutableListOf(model)
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
        currentList?.apply {
            add(model)
            notifyDataSetChanged()
        }

    }

    open fun add(position: Int, model: T?) {
        model ?: return
        currentList?.apply {
            add(position, model)
            notifyDataSetChanged()
        }
    }

    open fun edit(position: Int, model: T?) {
        model ?: return
        if (position in 0..lastIndex) {
            currentList?.set(position, model)
            notifyItemChanged(position)
        }
    }

    open fun remove(index: Int) {
        currentList?.apply {
            removeAt(index)
            notifyItemRemoved(index)
        }
    }

    open fun remove(model: T?) {
        model ?: return
        val position = currentList?.indexOf(model) ?: -1
        if (position in 0..lastIndex) {
            currentList?.remove(model)
            notifyItemRemoved(position)
        }
    }

    open fun clear() {
        currentList?.clear()
        notifyDataSetChanged()
    }

    open fun unBind() {
        currentList = emptyData
        notifyDataSetChanged()
    }


    /**
     * Binding
     */
    open fun bind(recyclerView: RecyclerView, block: (LinearLayoutManager.() -> Unit) = {}) {
        recyclerView.initLayoutManager(block)
        recyclerView.adapter = this
    }

    open fun bind(recyclerView: RecyclerView, spanCount: Int, includeEdge: Boolean = true, block: (GridLayoutManager.() -> Unit) = {}) {
        val lm = recyclerView.initLayoutManager(spanCount, block)
        GridDecoration.draw(recyclerView, lm.spanCount, 0, includeEdge)
        recyclerView.adapter = this
    }


    /**
     * Utils
     */
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

}
