package com.huy.kotlin.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huy.kotlin.R
import com.huy.library.extension.addOnClickListener

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
     * [BaseRecyclerAdapter] abstract function for initialize recycler view type.
     */
    @LayoutRes
    protected abstract fun layoutResource(model: T, position: Int): Int

    protected abstract fun View.onBindModel(model: T, position: Int, @LayoutRes layout: Int)

    open fun View.onFirstBindModel(model: T, position: Int, @LayoutRes layout: Int) {
        onBindModel(model, position, layout)
    }


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
            blankItemVisible?.also { it(viewHolder.itemView) }
            return
        }

        if (type == footerLayoutResource) {
            if (position.isNotIndexed()) footerIndexChange?.also { it(viewHolder.itemView, position) }
            return
        }

        val model = get(position) ?: return

        if (position.isNotIndexed()) viewHolder.itemView.onFirstBindModel(model, position, type)
        else viewHolder.itemView.onBindModel(model, position, type)

        position.updateLastIndex()

        viewHolder.itemView.addOnClickListener {
            itemClick?.also { it(model, position) }
        }

        viewHolder.itemView.setOnLongClickListener {
            itemLongClick?.also { it(model, position) }
            return@setOnLongClickListener true
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

    private var footerIndexChange: ((View, Int) -> Unit)? = null

    open fun onFooterIndexChange(block: ((View, Int) -> Unit)) {
        footerIndexChange = block
    }

    private var blankItemVisible: ((View) -> Unit)? = null

    open fun onBlankItemVisible(block: ((View) -> Unit)) {
        blankItemVisible = block
    }

    private var lastIndexed: Int = -1

    private fun Int.isNotIndexed(): Boolean {
        return this > lastIndexed
    }

    private fun Int.updateLastIndex() {
        if (this > lastIndexed) lastIndexed = this
    }


    /**
     * Data list handle.
     */
    private val emptyData: MutableList<T> = mutableListOf()

    var currentList: MutableList<T> = mutableListOf()

    open val size: Int get() = currentList.size

    open val dataIsEmpty: Boolean get() = currentList.isEmpty()

    open val dataNotEmpty: Boolean get() = currentList.isNotEmpty()

    open val lastPosition: Int get() = if (currentList.isEmpty()) -1 else (currentList.size - 1)

    open fun indexInBound(position: Int): Boolean {
        return position > -1 && position < size
    }

    open fun indexOutBound(position: Int): Boolean {
        return position < 0 || position >= size
    }

    open fun get(position: Int): T? {
        if (indexInBound(position)) return currentList[position]
        return null
    }

    open fun set(collection: Collection<T>?) {
        currentList = collection?.toMutableList() ?: emptyData
        lastIndexed = -1
        notifyDataSetChanged()
    }

    open fun set(list: MutableList<T>?) {
        currentList = list ?: emptyData
        lastIndexed = -1
        notifyDataSetChanged()
    }

    open fun set(array: Array<T>?) {
        currentList = array?.toMutableList() ?: emptyData
        lastIndexed = -1
        notifyDataSetChanged()
    }

    open fun set(model: T?) {
        currentList = if (model == null) emptyData
        else mutableListOf(model)
        lastIndexed = -1
        notifyDataSetChanged()
    }

    open fun setElseEmpty(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        currentList = collection.toMutableList()
        lastIndexed = -1
        notifyDataSetChanged()
    }

    open fun setElseEmpty(list: MutableList<T>?) {
        if (list.isNullOrEmpty()) return
        currentList = list
        lastIndexed = -1
        notifyDataSetChanged()
    }

    open fun setElseEmpty(array: Array<T>?) {
        if (array == null || array.isEmpty()) return
        currentList = array.toMutableList()
        lastIndexed = -1
        notifyDataSetChanged()
    }

    open fun setElseEmpty(model: T?) {
        model ?: return
        currentList = mutableListOf(model)
        lastIndexed = -1
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
        if (indexInBound(index)) {
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
        if (indexInBound(index)) {
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

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

}
