package sample.ui.selectable

import androidx.viewbinding.ViewBinding
import com.kotlin.app.databinding.SelectableItemBinding
import com.sample.library.recycler.BaseListAdapter
import com.sample.library.recycler.ItemInflating
import com.sample.widget.extension.bold
import com.sample.widget.extension.hide
import com.sample.widget.extension.regular
import com.sample.widget.extension.show


class SelectableAdapter<T> : BaseListAdapter<T>() {

    var selectedPosition: Int = -1

    var onItemText: (T) -> String = { it.toString() }

    var onItemSelected: (T) -> Unit = {}

    override fun itemInflating(item: T, position: Int): ItemInflating {
        return SelectableItemBinding::inflate
    }

    override fun ViewBinding.onBindItem(item: T, position: Int) {
        (this as? SelectableItemBinding?)?.apply {
            if (position != selectedPosition) {
                onBindModelUnselected(item)
            } else {
                onBindModelSelected(item)
            }
            textViewItem.text = onItemText(item)
        }
    }

    fun SelectableItemBinding.onBindModel(item: T) {
        textViewItem.text = onItemText(item)
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

    fun notifySelectionChanged(position: Int) {
        notifyItemChanged(selectedPosition)
        selectedPosition = position
        notifyItemChanged(position)
        get(position)?.also { onItemSelected(it) }
    }


}

