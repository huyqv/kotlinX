package com.huy.kotlin.base.adapter

import android.view.View
import androidx.core.content.ContextCompat
import com.huy.kotlin.R
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

    var selectedItems: MutableList<M> = mutableListOf()

    var optional: Boolean = true

    var onSelectionChanged: ((M?, Int, Boolean) -> Unit)? = null

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

    open fun onSelectionChanged(block: (M?, Int, Boolean) -> Unit) {
        onSelectionChanged = block
    }

    open fun isSelected(model: M): Boolean {

        model ?: return false

        if (dataIsEmpty) return false

        if (selectedItems.isEmpty()) return false

        for (m in selectedItems) if (model == m) return true

        return false
    }

    open fun onItemClick(model: M, position: Int, isSelected: Boolean) {

        if (optional) {
            if (isSelected) selectedItems.remove(model)
            else selectedItems.add(model)
            onSelectionChanged?.apply { this(model, position, !isSelected) }
            notifyItemChanged(position)
            return
        }

        if (isSelected) {
            if (selectedItems.size < 2) return
            selectedItems.remove(model)
            onSelectionChanged?.apply { this(model, position, false) }
        } else {
            selectedItems.add(model)
            onSelectionChanged?.apply { this(model, position, true) }
        }

        notifyItemChanged(position)

    }

    open fun setSelectedItem(list: Collection<M>?) {

        if (optional && list != null) {
            selectedItems = list.toMutableList()
            notifyDataSetChanged()
            return
        }

        if (list == null || list.isEmpty()) {
            selectedItems = currentList
            notifyDataSetChanged()
        }
    }

}