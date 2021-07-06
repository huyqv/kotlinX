package sample.ui.selectable

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.library.recycler.initLayoutManager
import com.example.library.extension.isGone
import com.kotlin.app.R
import com.kotlin.app.databinding.SelectableBinding
import sample.ui.main.MainDialogFragment

class SelectableFragment : MainDialogFragment<SelectableBinding>() {

    override fun inflating(): (LayoutInflater) -> SelectableBinding {
        return SelectableBinding::inflate
    }

    override fun onViewCreated() {
        addClickListener(bind.viewClose)
        bind.recyclerView.initLayoutManager()
        bind.recyclerView.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
    }

    override fun onLiveDataObserve() {
        dialogVM.selectableLiveData.observe {
            if (null != it) {
                bind.textViewTitle.text = it.title ?: getString(R.string.app_name)
                bind.editTextSearch.isGone(!it.isSearchable)
                onBindListItem(it.adapter)
            } else {
                dismissAllowingStateLoss()
            }
        }
    }

    override fun onViewClick(view: View?) {
        when (view) {
            bind.viewClose -> dismiss()
        }
    }

    /**
     * [SelectableFragment] properties
     */
    private fun onBindListItem(adapter: SelectableAdapter<*>) {
        adapter.onItemClick = { _, position ->
            adapter.notifySelectionChanged(position)
            setNavResult(dialogVM.selectableLiveData.value?.key, position)
            navigateUp()
        }
        bind.recyclerView.adapter = adapter
    }

}