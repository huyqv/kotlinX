package com.huy.kotlin.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.*
import com.huy.kotlin.R
import com.huy.library.extension.addOnClickListener

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/10/06
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BasePagedListAdapter<T> : PagedListAdapter<T, RecyclerView.ViewHolder> {


    private val differ: AsyncPagedListDiffer<T>

    constructor(itemCallback: DiffUtil.ItemCallback<T> = DiffItemCallback()) : super(itemCallback) {
        differ = asyncPagedListDiffer(itemCallback)
    }


    /**
     * [BasePagedListAdapter] abstract function for initialize recycler view type.
     */
    @LayoutRes
    protected abstract fun layoutResource(model: T, position: Int): Int

    protected abstract fun View.onBindModel(model: T, position: Int, @LayoutRes layout: Int)

    open fun View.onFirstBindModel(model: T, position: Int, @LayoutRes layout: Int) {
        onBindModel(model, position, layout)
    }


    /**
     * [PagedListAdapter] override.
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

    override fun getCurrentList(): PagedList<T>? {
        return differ.currentList
    }

    override fun submitList(pagedList: PagedList<T>?) {
        if (pagedList.isNullOrEmpty()) return
        else differ.submitList(pagedList)
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

    var footerLayoutRes: Int
        get() = footerLayoutResource
        set(value) {
            footerLayoutResource = value
            notifyItemChanged(size)
        }

    fun hideFooter() {
        footerLayoutRes = 0
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
    open val size: Int get() = currentList?.size ?: 0

    open val dataIsEmpty: Boolean get() = size == 0

    open val dataNotEmpty: Boolean get() = size != 0

    open val lastPosition: Int get() = if (dataIsEmpty) -1 else (size - 1)

    open fun indexInBound(position: Int): Boolean {
        return position > -1 && position < size
    }

    open fun indexOutBound(position: Int): Boolean {
        return position < 0 || position >= size
    }

    open fun get(position: Int): T? {
        if (currentList.isNullOrEmpty()) return null
        if (indexInBound(position)) return differ.getItem(position)
        return null
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

    private fun asyncPagedListDiffer(itemCallback: DiffUtil.ItemCallback<T>): AsyncPagedListDiffer<T> {

        val adapterCallback = AdapterListUpdateCallback(this)
        val listCallback = object : ListUpdateCallback {
            override fun onChanged(position: Int, count: Int, payload: Any?) {
                adapterCallback.onChanged(position + 1, count, payload)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                adapterCallback.onMoved(fromPosition + 1, toPosition + 1)
            }

            override fun onInserted(position: Int, count: Int) {
                adapterCallback.onInserted(position + 1, count + 1)
            }

            override fun onRemoved(position: Int, count: Int) {
                adapterCallback.onRemoved(position + 1, count)
            }
        }
        return AsyncPagedListDiffer<T>(listCallback, AsyncDifferConfig.Builder<T>(itemCallback).build())
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

}

