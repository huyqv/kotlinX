package com.huy.kotlin.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.*
import com.huy.kotlin.R
import com.huy.library.extension.preventClick


abstract class BaseListAdapter<T>(itemCallback: DiffUtil.ItemCallback<T>) : ListAdapter<T, RecyclerView.ViewHolder>(itemCallback) {


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

        return if (blankLayoutResource != 0 || footerLayoutResource != 0) sizeCache + 1
        else sizeCache
    }

    override fun getItemViewType(position: Int): Int {

        if (dataIsEmpty && blankLayoutResource != 0) return blankLayoutResource

        if (dataNotEmpty && footerLayoutResource != 0 && position == sizeCache) return footerLayoutResource

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

        if (position.isNotIndexed()) viewHolder.itemView.onBindModel(model, position, type)
        else viewHolder.itemView.onFirstBindModel(model, position, type)

        position.updateLastIndex()

        viewHolder.itemView.setOnClickListener {
            it.preventClick(300)
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
        notifyItemChanged(sizeCache)
    }

    open fun hideFooter() {
        footerLayoutResource = 0
        notifyItemChanged(sizeCache)
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
    var data: MutableList<T> = mutableListOf()

    var sizeCache = 0

    open val dataIsEmpty: Boolean get() = data.isEmpty()

    open val dataNotEmpty: Boolean get() = data.isNotEmpty()

    open val lastPosition: Int get() = if (data.isEmpty()) -1 else (data.size - 1)

    open fun indexInBound(position: Int): Boolean {
        return position > -1 && position < sizeCache
    }

    open fun get(position: Int): T? {
        if (indexInBound(position)) return data[position]
        return null
    }

    open fun resize() {
        sizeCache = data.size
    }

    open fun update(collection: Collection<T>?) {
        data = collection?.toMutableList() ?: mutableListOf()
        lastIndexed = -1
    }

    open fun set(collection: Collection<T>?) {
        update(collection)
        notifyDataChanged()
    }

    open fun set(collection: MutableList<T>?) {
        data = collection ?: mutableListOf()
        lastIndexed = -1
        notifyDataChanged()
    }

    open fun set(array: Array<T>?) {
        data = array?.toMutableList() ?: mutableListOf()
        lastIndexed = -1
        notifyDataChanged()
    }

    open fun set(model: T?) {
        data = if (model == null) mutableListOf()
        else mutableListOf(model)
        lastIndexed = -1
        notifyDataChanged()
    }

    open fun setElseEmpty(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        data = collection.toMutableList()
        lastIndexed = -1
        notifyDataChanged()
    }

    open fun setElseEmpty(mutableList: MutableList<T>?) {
        if (mutableList.isNullOrEmpty()) return
        data = mutableList
        lastIndexed = -1
        resize()
        notifyDataSetChanged()
    }

    open fun setElseEmpty(array: Array<T>?) {
        if (array == null || array.isEmpty()) return
        data = array.toMutableList()
        lastIndexed = -1
        notifyDataChanged()
    }

    open fun setElseEmpty(model: T?) {
        model ?: return
        data = mutableListOf(model)
        lastIndexed = -1
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
        val s = sizeCache
        resize()
        notifyItemRangeChanged(s, sizeCache + 1)
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
                return if (dataIsEmpty || position == sizeCache) layoutManager.spanCount
                else 1
            }
        }
        block?.let { layoutManager.block() }
        recyclerView.layoutManager = layoutManager
        GridDecoration.draw(recyclerView, layoutManager.spanCount, 0, includeEdge)
        recyclerView.adapter = this
    }

}