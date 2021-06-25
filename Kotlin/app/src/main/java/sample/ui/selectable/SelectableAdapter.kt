package sample.ui.selectable

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.example.library.recycler.BaseListAdapter
import com.example.library.extension.bold
import com.example.library.extension.hide
import com.example.library.extension.regular
import com.example.library.extension.show
import com.kotlin.app.databinding.SelectableItemBinding


class SelectableAdapter<T> : BaseListAdapter<T>() {

    var selectedPosition: Int = -1

    var onItemText: (T) -> String = { it.toString() }

    var onItemSelected: (T) -> Unit = {}

    override fun itemInflating(item: T, position: Int): (LayoutInflater) -> ViewBinding {
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

    fun SelectableItemBinding.onBindModel(item: T, position: Int, layout: Int) {
        textViewItem.text = onItemText(item)
    }

    private fun SelectableItemBinding.onBindModelSelected(item: T) {
        imageViewSelected.show()
        textViewItem.bold()
    }

    private fun SelectableItemBinding.onBindModelUnselected(item: T) {
        imageViewSelected.hide()
        textViewItem.regular()
    }

    fun notifySelectionChanged(position: Int) {
        notifyItemChanged(selectedPosition)
        selectedPosition = position
        notifyItemChanged(position)
        get(position)?.also { onItemSelected(it) }
    }


}

