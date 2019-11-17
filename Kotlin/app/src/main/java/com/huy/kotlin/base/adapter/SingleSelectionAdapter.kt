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
open class SingleSelectionAdapter<M> : BaseRecyclerAdapter<M>() {

    var selectedItem: M? = null

    var optional: Boolean = true

    var onSelectionChanged: ((M?, Int, Boolean) -> Unit)? = null

    override fun footerLayoutResource() = 0

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

        if (dataIsEmpty()) return false

        selectedItem ?: return false

        return selectedItem == model

    }

    open fun onItemClick(model: M, position: Int, isSelected: Boolean) {

        if (optional) {
            selectedItem = if (isSelected) null else model
            onSelectionChanged?.apply { this(model, position, !isSelected) }
            notifyDataSetChanged()
            return
        }

        if (isSelected) return
        selectedItem = model
        onSelectionChanged?.apply { this(model, position, true) }
        notifyDataSetChanged()
        return
    }

}