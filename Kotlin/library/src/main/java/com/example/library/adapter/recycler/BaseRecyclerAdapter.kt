package com.example.library.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.library.extension.addViewClickListener

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
    var currentList: MutableList<T> = mutableListOf()

    var lastBindIndex: Int = -1

    val lastIndex: Int get() = currentList.lastIndex

    val size: Int get() = currentList.size

    val dataIsEmpty: Boolean get() = currentList.isNullOrEmpty()

    val dataNotEmpty: Boolean get() = !dataIsEmpty

    /**
     * List update
     */
    open fun get(position: Int): T? {
        if (position in 0..lastIndex) return currentList[position]
        return null
    }

    open fun set(collection: Collection<T>?) {
        currentList = collection?.toMutableList() ?: mutableListOf()
        lastBindIndex = -1
        notifyDataSetChanged()
    }

    open fun set(list: MutableList<T>?) {
        currentList = list ?: mutableListOf()
        lastBindIndex = -1
        notifyDataSetChanged()
    }

    open fun set(array: Array<T>?) {
        currentList = array?.toMutableList() ?: mutableListOf()
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
        currentList.addAll(collection)
        notifyDataSetChanged()
    }

    open fun add(array: Array<T>?) {
        if (array.isNullOrEmpty()) return
        currentList.addAll(array)
        notifyDataSetChanged()
    }

    open fun add(model: T?) {
        model ?: return
        currentList.add(model)
        notifyItemRangeChanged(size, size + 1)
    }

    open fun add(position: Int, model: T?) {
        model ?: return
        currentList.add(position, model)
        notifyDataSetChanged()
    }

    open fun edit(position: Int, model: T?) {
        model ?: return
        if (position in 0..lastIndex) {
            currentList[position] = model
            notifyItemChanged(position)
        }
    }

    open fun remove(index: Int) {
        currentList.removeAt(index)
        notifyItemRemoved(index)
    }

    open fun remove(model: T?) {
        model ?: return
        val position = currentList.indexOf(model)
        remove(position)
    }

    open fun clear() {
        currentList = mutableListOf()
        notifyDataSetChanged()
    }

}
