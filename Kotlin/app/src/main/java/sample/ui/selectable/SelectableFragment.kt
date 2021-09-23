package sample.ui.selectable

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.kotlin.app.R
import com.kotlin.app.databinding.SelectableBinding
import com.sample.library.adapter.initLayoutManager
import com.sample.widget.extension.isGone
import sample.ui.main.MainDialogFragment

class SelectableFragment : MainDialogFragment<SelectableBinding>() {

    override fun inflating(): (LayoutInflater) -> ViewBinding {
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

    override fun onViewClick(v: View?) {
        when (v) {
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
            onBackPressed()
        }
        bind.recyclerView.adapter = adapter
    }

}