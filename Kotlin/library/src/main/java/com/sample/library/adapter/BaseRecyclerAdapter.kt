package com.sample.library.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
        BaseAdapter<T> {

    /**
     * [PagingDataAdapter] implements
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

    /**
     *
     */
    private var currentList: MutableList<T> = mutableListOf()

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
    open fun set(collection: Collection<T>?) {
        currentList = collection?.toMutableList() ?: mutableListOf()
        Log.d("BRA", "=====================")
        currentList.forEach {
            Log.d("BRA", it.toString())
        }
        lastBindIndex = -1
        //notifyItemRangeChanged(0, itemCount-1)
        notifyDataSetChanged()
    }

    open fun set(array: Array<T>?) {
        currentList = array?.toMutableList() ?: mutableListOf()
        currentList.forEach {
            Log.d("BRA", it.toString())
        }
        lastBindIndex = -1
        notifyDataSetChanged()
    }

    open fun setElseEmpty(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        set(collection)
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