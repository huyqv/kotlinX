package com.example.kotlin.ui.selectable

import android.view.View
import com.example.kotlin.R
import com.example.library.adapter.recycler.BaseRecyclerAdapter
import com.example.library.extension.bold
import com.example.library.extension.hide
import com.example.library.extension.regular
import com.example.library.extension.show
import kotlinx.android.synthetic.main.selectable_item.view.*


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/09
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SelectableAdapter<T> : BaseRecyclerAdapter<T>() {

    var selectedPosition: Int = -1

    var onItemText: (T) -> String = { it.toString() }

    var onItemSelected: (T) -> Unit = {}

    override fun layoutResource(model: T, position: Int): Int {
        return R.layout.selectable_item
    }

    override fun View.onBindModel(model: T, position: Int, layout: Int) {
        selectableTextViewItem.text = onItemText(model)
        val isSelected = position == selectedPosition
        if (isSelected) {
            selectableViewSelected.show()
            selectableTextViewItem.bold()
        } else {
            selectableViewSelected.hide()
            selectableTextViewItem.regular()
        }
    }

    fun notifySelectionChanged(position: Int) {
        notifyItemChanged(selectedPosition)
        selectedPosition = position
        notifyItemChanged(position)
        get(position)?.also { onItemSelected(it) }
    }

}