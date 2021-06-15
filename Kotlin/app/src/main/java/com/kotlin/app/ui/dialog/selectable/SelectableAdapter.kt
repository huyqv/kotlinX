package com.kotlin.app.ui.dialog.selectable

import android.view.View
import com.example.library.adapter.recycler.BaseListAdapter
import com.example.library.extension.bold
import com.example.library.extension.hide
import com.example.library.extension.regular
import com.example.library.extension.show
import com.kotlin.app.R
import kotlinx.android.synthetic.main.selectable_item.view.*

class SelectableAdapter<T> : BaseListAdapter<T>() {

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
            onBindModelSelected(model)
        } else {
            onBindModelUnselected(model)
        }
    }

    private fun View.onBindModelSelected(model: T) {
        selectableViewSelected.show()
        selectableTextViewItem.bold()
    }

    private fun View.onBindModelUnselected(model: T) {
        selectableViewSelected.hide()
        selectableTextViewItem.regular()
    }

    fun notifySelectionChanged(position: Int) {
        notifyItemChanged(selectedPosition)
        selectedPosition = position
        notifyItemChanged(position)
        get(position)?.also { onItemSelected(it) }
    }

}