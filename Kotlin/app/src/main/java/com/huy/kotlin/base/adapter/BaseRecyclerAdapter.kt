package com.huy.kotlin.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huy.kotlin.R
import com.huy.library.extension.preventClick

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/18
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseRecyclerAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    /**
     * [BaseRecyclerAdapter] abstract function for initialize recycler view type.
     */
    @LayoutRes
    protected abstract fun layoutResource(model: T, position: Int): Int

    protected abstract fun View.onBindModel(model: T, position: Int, @LayoutRes layout: Int)


    /**
     * [RecyclerView.Adapter] override.
     */
    override fun getItemCount(): Int {

        return if (blankLayoutResource != 0 || footerLayoutResource != 0) size + 1
        else size
    }

    override fun getItemViewType(position: Int): Int {

        if (dataIsEmpty() && blankLayoutResource != 0) return blankLayoutResource

        if (dataNotEmpty() && footerLayoutResource != 0 && position == size) return footerLayoutResource

        val model = get(position) ?: return R.layout.view_gone

        return layoutResource(model, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val type = getItemViewType(position)

        if (type == R.layout.view_gone) return

        if (type == blankLayoutResource) {
            blankVisible?.run { blankVisible!!(viewHolder.itemView) }
            return
        }

        if (type == footerLayoutResource) {
            footerVisible?.run {
                if (footerIndexed == position) return
                footerIndexed = position
                footerVisible!!(viewHolder.itemView, position)
            }
            return
        }

        val model = get(position) ?: return

        viewHolder.itemView.onBindModel(model, position, type)

        itemClick?.also { block ->
            viewHolder.itemView.setOnClickListener {
                it.preventClick(300)
                block(model, position)
            }
        }

        itemLongClick?.also { block ->
            viewHolder.itemView.setOnLongClickListener {
                block(model, position)
                return@setOnLongClickListener true
            }
        }

    }


    /**
     * Layout resource for empty data.
     */
    private var blankLayoutResource = blankLayoutResource()

    @LayoutRes
    open fun blankLayoutResource(): Int {
        return 0
    }


    /**
     * Layout resource for footer item.
     */
    @Volatile
    private var footerIndexed: Int = -1

    private var footerLayoutResource = footerLayoutResource()

    @LayoutRes
    open fun footerLayoutResource(): Int {
        return 0
    }

    open fun showFooter(@LayoutRes res: Int) {
        footerLayoutResource = res
        notifyItemChanged(size)
    }

    open fun hideFooter() {
        footerLayoutResource = 0
        notifyItemChanged(size)
    }


    /**
     * User interfaces.
     */
    private var itemClick: ((T, Int) -> Unit)? = null

    open fun onItemClick(block: (T, Int) -> Unit) {
        itemClick = block
    }

    private var itemLongClick: ((T, Int) -> Unit)? = null

    open fun onItemLongClick(block: (T, Int) -> Unit) {
        itemLongClick = block
    }

    // footerLayoutResource() != 0
    private var footerVisible: ((View, Int) -> Unit)? = null

    open fun onBindFooter(block: ((View, Int) -> Unit)) {
        footerVisible = block
    }

    // blankLayoutResource() != 0
    private var blankVisible: ((View) -> Unit)? = null

    open fun onBindBlank(block: ((View) -> Unit)) {
        blankVisible = block
    }


    /**
     * Data list handle.
     */
    var data: MutableList<T> = mutableListOf()

    var size = 0

    open fun indexInBound(position: Int): Boolean {
        return position > -1 && position < size
    }

    open fun get(position: Int): T? {
        if (indexInBound(position)) return data[position]
        return null
    }

    open fun resize() {
        size = data.size
    }

    open fun dataIsEmpty(): Boolean {
        return data.isEmpty()
    }

    open fun dataNotEmpty(): Boolean {
        return data.isNotEmpty()
    }

    open fun lastPosition(): Int {
        return if (data.isEmpty()) -1 else (data.size - 1)
    }

    open fun set(collection: Collection<T>?) {
        data = collection?.toMutableList() ?: mutableListOf()
        notifyDataChanged()
    }

    open fun set(collection: MutableList<T>?) {
        data = collection ?: mutableListOf()
        notifyDataChanged()
    }

    open fun set(array: Array<T>?) {
        data = array?.toMutableList() ?: mutableListOf()
        notifyDataChanged()
    }

    open fun set(model: T?) {
        data = if (model == null) mutableListOf()
        else mutableListOf(model)
        notifyDataChanged()
    }

    open fun setElseEmpty(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        data = collection.toMutableList()
        notifyDataChanged()
    }

    open fun setElseEmpty(mutableList: MutableList<T>?) {
        if (mutableList.isNullOrEmpty()) return
        data = mutableList
        resize()
        notifyDataSetChanged()
    }

    open fun setElseEmpty(array: Array<T>?) {
        if (array == null || array.isEmpty()) return
        data = array.toMutableList()
        notifyDataChanged()
    }

    open fun setElseEmpty(model: T?) {
        model ?: return
        data = mutableListOf(model)
        notifyDataChanged()
    }

    open fun add(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        data.addAll(collection)
        notifyRangeChanged()
    }

    open fun add(array: Array<T>?) {
        if (array == null || array.isEmpty()) return
        data.addAll(array)
        notifyRangeChanged()
    }

    open fun add(model: T?) {
        model ?: return
        data.add(model)
        notifyRangeChanged()
    }

    open fun addFirst(model: T?) {
        model ?: return
        data.add(0, model)
        notifyDataChanged()
    }

    open fun edit(index: Int, model: T?) {
        model ?: return
        if (indexInBound(index)) {
            data[index] = model
            notifyItemChanged(index)
        }
    }

    open fun remove(index: Int) {
        data.removeAt(index)
        resize()
        notifyItemRemoved(index)
    }

    open fun remove(model: T?) {
        model ?: return
        val index = data.indexOf(model)
        if (indexInBound(index)) {
            data.remove(model)
            resize()
            notifyItemRemoved(index)
        }
    }

    open fun clear() {
        data.clear()
        notifyDataChanged()
    }

    open fun unBind() {
        data = mutableListOf()
        notifyDataChanged()
    }

    open fun notifyDataChanged() {
        resize()
        notifyDataSetChanged()
    }

    open fun notifyRangeChanged() {
        val s = size
        resize()
        notifyItemRangeChanged(s, size + 1)
    }

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
                return if (dataIsEmpty() || position == size) layoutManager.spanCount
                else 1
            }
        }
        block?.let { layoutManager.block() }
        recyclerView.layoutManager = layoutManager
        GridDecoration.draw(recyclerView, layoutManager.spanCount, 0, includeEdge)
        recyclerView.adapter = this
    }

}
