package com.example.kotlin.ui.adapter

import android.view.View
import androidx.core.content.ContextCompat
import com.example.kotlin.R
import com.example.library.adapter.recycler.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.item_text.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/08/29
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
open class MultiSelectionAdapter<M> : BaseRecyclerAdapter<M>() {

    var selectedItems: MutableList<M>? = null

    var optional: Boolean = true

    var onSelectionChanged: (M?, Int, Boolean) -> Unit = { _, _, _ -> }

    override fun layoutResource(model: M, position: Int) = R.layout.item_text

    override fun View.onBindModel(model: M, position: Int, layout: Int) {
        val isSelected = isSelected(model)
        if (isSelected) bindStateSelected(model)
        else bindStateUnselected(model)
        setOnClickListener {
            onItemClick(model, position, isSelected)
        }
    }

    protected open fun View.bindStateSelected(model: M) {
        itemTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        itemTextView.text = model.toString()
    }

    protected open fun View.bindStateUnselected(model: M) {
        itemTextView.setTextColor(ContextCompat.getColor(context, R.color.colorTextSecondary))
        itemTextView.text = model.toString()
    }

    open fun isSelected(model: M): Boolean {
        model ?: return false
        if (dataIsEmpty) return false
        if (selectedItems.isNullOrEmpty()) return false
        for (m in selectedItems!!) if (model == m) return true
        return false
    }

    open fun onItemClick(model: M, position: Int, isSelected: Boolean) {

        if (optional) selectedItems?.also {
            if (isSelected) it.remove(model)
            else it.add(model)
            onSelectionChanged(model, position, !isSelected)
            notifyItemChanged(position)
            return
        }

        if (isSelected) selectedItems?.also {
            if (it.size < 2) return
            it.remove(model)
            onSelectionChanged(model, position, false)
            return
        }
        val items = selectedItems ?: mutableListOf(model)
        items.add(model)
        selectedItems = items
        onSelectionChanged(model, position, true)
        notifyItemChanged(position)
    }

    open fun setSelectedItem(list: Collection<M>?) {
        if (optional && list != null) {
            selectedItems = list.toMutableList()
            notifyDataSetChanged()
            return
        }
        if (list.isNullOrEmpty()) {
            selectedItems = currentList
            notifyDataSetChanged()
        }
    }

}