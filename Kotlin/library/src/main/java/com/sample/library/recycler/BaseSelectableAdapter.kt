package com.sample.library.recycler

import androidx.viewbinding.ViewBinding

abstract class BaseSelectableAdapter<T> : BaseListAdapter<T>() {

    var selectedItem: T? = null
        set(value) {
            field = value
            if(!areSameItems(value, selectedItem)) {
                notifyDataSetChanged()
            }
        }

    val selectedPosition: Int
        get() {
            selectedItem ?: return -1
            return currentList.indexOf(selectedItem)
        }

    var onSelectionChanged: ((T, Int) -> Unit)? = null

    override var onItemClick: (T, Int) -> Unit = { item, position ->
        updateSelectedItem(item, position)
    }

    val itemClickList = mutableListOf<(T, Boolean) -> Unit>()

    fun addOnItemClick(block: (T /*item*/, Boolean/*hasChange*/) -> Unit) {
        itemClickList.add(block)
    }

    private fun updateSelectedItem(item: T, position: Int) {
        if(isSelected(item)) {
            itemClickList.forEach { it(item, false) }
            return
        }
        val tempSelectedPosition = selectedPosition
        if(selectedPosition in 0..lastIndex) {
            notifyItemChanged(tempSelectedPosition)
        }
        selectedItem = item
        notifyItemChanged(position)
        onSelectionChanged?.invoke(item, position)
        itemClickList.forEach { it(item, true) }
    }

    final override fun ViewBinding.onBindItem(item: T, position: Int) {
        onBindDefaultItem(item, position)
        if(isSelected(item)) {
            onBindSelectedItem(item, position)
        } else {
            onBindUnselectedItem(item, position)
        }
    }

    open fun isSelected(item: T?): Boolean {
        return areSameItems(item, selectedItem)
    }

    open fun areSameItems(item: T?, other: T?): Boolean {
        return item == other
    }

    abstract fun ViewBinding.onBindDefaultItem(item: T, position: Int)

    abstract fun ViewBinding.onBindSelectedItem(item: T, position: Int)

    abstract fun ViewBinding.onBindUnselectedItem(item: T, position: Int)

}