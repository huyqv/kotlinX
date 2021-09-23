package com.sample.library.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.*

abstract class BaseListAdapter<T> : ListAdapter<T, RecyclerView.ViewHolder>,
    BaseAdapter<T> {

    private val differ: AsyncListDiffer<T>

    constructor(itemCallback: DiffUtil.ItemCallback<T> = DiffItemCallback()) : super(itemCallback) {
        differ = asyncListDiffer(itemCallback)
    }

    /**
     * [ListAdapter] implements
     */
    override fun getItemCount(): Int {
        return extraItemCount
    }

    override fun getItemViewType(position: Int): Int {
        return getBaseItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return onBaseCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        onBaseBindViewHolder(viewHolder, position)
    }

    override fun getCurrentList(): MutableList<T> {
        return differ.currentList
    }

    override fun submitList(list: MutableList<T>?) {
        differ.submitList(list)
    }

    override fun submitList(list: MutableList<T>?, commitCallback: Runnable?) {
        differ.submitList(list, commitCallback)
    }

    /**
     * [BaseAdapter] implements
     */
    override var onItemClick: OnItemClick<T> = null

    override var onItemLongClick: OnItemClick<T> = null

    override var onFooterIndexChanged: ((Int) -> Unit)? = null

    override var lastBindIndex: Int = -1

    override fun listItem(): MutableList<T> {
        return currentList
    }

    /**
     * Utils
     */
    open fun submit() {
        set(currentList)
    }

    open fun set(collection: Collection<T>?, commitCallback: Runnable? = null) {
        lastBindIndex = -1
        submitList(collection?.toMutableList(), commitCallback)
    }

    open fun set(array: Array<T>?, commitCallback: Runnable? = null) {
        lastBindIndex = -1
        submitList(array?.toMutableList(), commitCallback)
    }

    open fun setElseEmpty(collection: Collection<T>?, commitCallback: Runnable? = null) {
        if (collection.isNullOrEmpty()) return
        set(collection, commitCallback)
    }

    open fun setElseEmpty(array: Array<T>?, commitCallback: Runnable? = null) {
        if (array.isNullOrEmpty()) return
        set(array, commitCallback)
    }

    private fun asyncListDiffer(itemCallback: DiffUtil.ItemCallback<T>): AsyncListDiffer<T> {

        val adapterCallback = AdapterListUpdateCallback(this)
        val listCallback = object : ListUpdateCallback {
            override fun onChanged(position: Int, count: Int, payload: Any?) {
                adapterCallback.onChanged(position, if (hasFooter) count + 1 else count, payload)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                adapterCallback.onMoved(fromPosition, toPosition)
            }

            override fun onInserted(position: Int, count: Int) {
                adapterCallback.onInserted(position, if (hasFooter) count + 1 else count)
            }

            override fun onRemoved(position: Int, count: Int) {
                adapterCallback.onRemoved(position, if (hasFooter) count + 1 else count)
            }
        }
        return AsyncListDiffer<T>(listCallback, AsyncDifferConfig.Builder<T>(itemCallback).build())
    }

}