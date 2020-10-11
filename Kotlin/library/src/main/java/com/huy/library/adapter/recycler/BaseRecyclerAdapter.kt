package com.huy.library.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
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
        if (dataIsEmpty && blankLayoutResource != 0) {
            return blankLayoutResource
        }
        if (dataNotEmpty && footerLayoutResource != 0 && position == size) {
            if (position > lastBindIndex) onFooterIndexChanged(position)
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

    var onFooterIndexChanged: (Int) -> Unit = {}

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
    var currentList: MutableList<T>? = null

    var lastBindIndex: Int = -1

    val lastIndex: Int get() = currentList?.lastIndex ?: -1

    val size: Int get() = currentList?.size ?: 0

    val dataIsEmpty: Boolean get() = currentList.isNullOrEmpty()

    val dataNotEmpty: Boolean get() = !dataIsEmpty

    /**
     * List update
     */
    open fun get(position: Int): T? {
        if (position in 0..lastIndex) return currentList?.get(position)
        return null
    }

    open fun set(collection: Collection<T>?) {
        currentList = collection?.toMutableList()
        lastBindIndex = -1
        notifyDataSetChanged()
    }

    open fun set(list: MutableList<T>?) {
        currentList = list
        lastBindIndex = -1
        notifyDataSetChanged()
    }

    open fun set(array: Array<T>?) {
        currentList = array?.toMutableList()
        lastBindIndex = -1
        notifyDataSetChanged()
    }

    open fun setElseEmpty(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        set(collection)
    }

    open fun setElseEmpty(list: MutableList<T>?) {
        if (list.isNullOrEmpty()) return
        set(list)
    }

    open fun setElseEmpty(array: Array<T>?) {
        if (array == null || array.isEmpty()) return
        set(array)
    }

    open fun add(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        currentList?.apply {
            addAll(collection)
            notifyDataSetChanged()
        }
    }

    open fun add(array: Array<T>?) {
        if (array.isNullOrEmpty()) return
        currentList?.apply {
            addAll(array)
            notifyDataSetChanged()
        }
    }

    open fun add(model: T?) {
        model ?: return
        currentList?.apply {
            add(model)
            notifyItemRangeChanged(size, size + 1)
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
        if (position in 0..lastIndex) currentList?.apply {
            set(position, model)
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
        remove(position)
    }

    open fun clear() {
        currentList = null
        notifyDataSetChanged()
    }

}
