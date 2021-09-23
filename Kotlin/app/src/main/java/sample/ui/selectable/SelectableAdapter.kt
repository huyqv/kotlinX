package sample.ui.selectable

import androidx.viewbinding.ViewBinding
import com.kotlin.app.R
import com.kotlin.app.databinding.SelectableItemBinding
import com.sample.library.adapter.BaseListAdapter
import com.sample.library.adapter.ItemOptions
import com.sample.widget.extension.bold
import com.sample.widget.extension.hide
import com.sample.widget.extension.regular
import com.sample.widget.extension.show

class SelectableAdapter<T> : BaseListAdapter<T>() {

    var selectedPosition: Int = -1

    var onItemText: (T) -> String = { it.toString() }

    var onItemSelected: (T) -> Unit = {}

    fun notifySelectionChanged(position: Int) {
        notifyItemChanged(selectedPosition)
        selectedPosition = position
        notifyItemChanged(position)
        getItemOrNull(position)?.also { onItemSelected(it) }
    }

    override fun modelItemOptions(item: T, position: Int): ItemOptions? {
        return ItemOptions(R.layout.selectable_item, SelectableItemBinding::bind)
    }

    override fun ViewBinding.onBindModelItem(item: T, position: Int) {
        (this as? SelectableItemBinding?)?.apply {
            if (position != selectedPosition) {
                onBindModelUnselected(item)
            } else {
                onBindModelSelected(item)
            }
            textViewItem.text = onItemText(item)
        }
    }

    private fun SelectableItemBinding.onBindModelSelected(item: T) {
        imageViewSelected.show()
        textViewItem.bold()
        textViewItem.text = item.toString()
    }

    private fun SelectableItemBinding.onBindModelUnselected(item: T) {
        imageViewSelected.hide()
        textViewItem.regular()
        textViewItem.text = item.toString()
    }

}

