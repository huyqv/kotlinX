package com.huy.library.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.*
import com.huy.library.extension.addViewClickListener

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
     * [PagedListAdapter] override
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

    override fun getCurrentList(): PagedList<T>? {
        return differ.currentList
    }

    override fun submitList(pagedList: PagedList<T>?) {
        if (pagedList.isNullOrEmpty()) return
        else differ.submitList(pagedList)
    }


    /**
     * [BasePagedListAdapter] abstractions
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
     * View click
     */
    var onItemClick: (T, Int) -> Unit = { _, _ -> }

    var onItemLongClick: (T, Int) -> Unit = { _, _ -> }


    /**
     * Position
     */
    private var lastIndexPosition: Int = -1

    private fun Int.updateLastIndex() {
        if (this > lastIndexPosition) lastIndexPosition = this
    }

    private val Int.isNotIndexed: Boolean get() = this > lastIndexPosition

    private val Int.indexInBound: Boolean get() = this > -1 && this < size

    private val Int.indexOutBound: Boolean get() = this < 0 || this >= size


    /**
     * Data
     */
    val size: Int get() = currentList?.size ?: 0

    val dataIsEmpty: Boolean get() = size == 0

    val dataNotEmpty: Boolean get() = size != 0

    val lastPosition: Int get() = if (dataIsEmpty) -1 else (size - 1)


    /**
     * List update
     */
    open fun get(position: Int): T? {
        if (currentList.isNullOrEmpty()) return null
        if (position.indexInBound) return differ.getItem(position)
        return null
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

