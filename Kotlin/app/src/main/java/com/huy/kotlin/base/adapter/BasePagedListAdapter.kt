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
import com.huy.library.extension.preventClick

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/10/06
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BasePagedListAdapter<T> constructor(itemCallback: DiffUtil.ItemCallback<T>) :
        PagedListAdapter<T, RecyclerView.ViewHolder>(itemCallback) {

    private val differ: AsyncPagedListDiffer<T>

    init {

        val adapterCallback = AdapterListUpdateCallback(this)

        val updateCallback = object : ListUpdateCallback {
            override fun onChanged(position: Int, count: Int, payload: Any?) {
                adapterCallback.onChanged(position, count, payload)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                adapterCallback.onMoved(fromPosition, toPosition)
            }

            override fun onInserted(position: Int, count: Int) {
                adapterCallback.onInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                adapterCallback.onRemoved(position, count)
            }
        }

        differ = AsyncPagedListDiffer<T>(updateCallback, AsyncDifferConfig.Builder<T>(itemCallback).build())
    }


    /**
     * [BasePagedListAdapter] abstract function for initialize recycler view type.
     */
    @LayoutRes
    protected abstract fun layoutResource(model: T, position: Int): Int

    protected abstract fun View.onBindModel(model: T, position: Int, @LayoutRes layout: Int)


    /**
     * [PagedListAdapter] override.
     */
    override fun getItemCount(): Int {

        return if (blankLayoutResource != 0 || footerLayoutResource != 0) size() + 1
        else size()
    }

    override fun getItemViewType(position: Int): Int {

        if (dataIsEmpty() && blankLayoutResource() != 0) return blankLayoutResource()

        if (dataNotEmpty() && footerLayoutResource != 0 && position == size()) return footerLayoutResource

        val model = getItem(position) ?: return R.layout.view_gone

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

        val model = getItem(position) ?: return

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

    override fun getItem(position: Int): T? {
        return get(position)
    }

    override fun submitList(pagedList: PagedList<T>?) {
        if (pagedList.isNullOrEmpty()) return
        else differ.submitList(pagedList)
    }

    override fun getCurrentList(): PagedList<T>? {
        return data
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
        notifyItemChanged(size())
    }

    open fun hideFooter() {
        footerLayoutResource = 0
        notifyItemChanged(size())
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

    private var footerVisible: ((View, Int) -> Unit)? = null

    open fun onBindFooter(block: ((View, Int) -> Unit)) {
        footerVisible = block
    }

    private var blankVisible: ((View) -> Unit)? = null

    open fun onBindBlank(block: ((View) -> Unit)) {
        blankVisible = block
    }


    /**
     * Data list handle.
     */
    val data: PagedList<T>?
        get() = differ.currentList

    open fun indexInBound(position: Int): Boolean {
        return position > -1 && position < size()
    }

    open fun size(): Int {
        return differ.currentList?.size ?: 0
    }

    open fun dataIsEmpty(): Boolean {
        return size() == 0
    }

    open fun dataNotEmpty(): Boolean {
        return size() != 0
    }

    open fun lastPosition(): Int {
        return if (dataIsEmpty()) -1 else (size() - 1)
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
                return if (dataIsEmpty() || position == size()) layoutManager.spanCount
                else 1
            }
        }
        block?.let { layoutManager.block() }
        recyclerView.layoutManager = layoutManager
        GridDecoration.draw(recyclerView, layoutManager.spanCount, 0, includeEdge)
        recyclerView.adapter = this
    }

}

