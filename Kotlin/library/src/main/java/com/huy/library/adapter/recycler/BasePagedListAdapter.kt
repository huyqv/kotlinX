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
        if (blankLayoutResource != 0 || footerLayoutResource != 0) {
            return size + 1
        }
        return size
    }

    override fun getItemViewType(position: Int): Int {
        if (dataIsEmpty && blankLayoutResource != 0) {
            return blankLayoutResource
        }
        if (dataNotEmpty && footerLayoutResource != 0 && position == size) {
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
     * Data
     */
    val size: Int get() = currentList?.size ?: 0

    var lastBindIndex: Int = -1

    val lastIndex: Int get() = currentList?.lastIndex ?: -1

    val dataIsEmpty: Boolean get() = size == 0

    val dataNotEmpty: Boolean get() = size != 0

    open fun get(position: Int): T? {
        if (position in 0..lastIndex) return differ.getItem(position)
        return null
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

}

